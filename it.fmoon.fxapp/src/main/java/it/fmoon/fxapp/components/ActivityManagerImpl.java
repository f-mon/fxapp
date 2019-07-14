package it.fmoon.fxapp.components;

import java.util.LinkedList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

import io.reactivex.Single;
import it.fmoon.fxapp.controllers.application.ControllerStackViewContainer;
import it.fmoon.fxapp.mvc.Activity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.BasePageImpl;
import it.fmoon.fxapp.mvc.BasePageImpl.StopOptions;
import it.fmoon.fxapp.mvc.Page;
import it.fmoon.fxapp.mvc.PageDef;

@Component
public class ActivityManagerImpl implements ActivityManager {

	private LinkedList<BasePageImpl> pagesStack = new LinkedList<>();
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	ControllerStackViewContainer stackViewContainer;
	
	
	@Override
	public Single<Page> startPage(PageDef pageDef) {
		return checkPausePage()
			.flatMap((r)->{				
				Page newPageInstance = pageDef.newPageInstance(applicationContext);
				BasePageImpl basePageImpl = (BasePageImpl)newPageInstance;
				basePageImpl.setParentPage(pagesStack.peekLast());
				pagesStack.add(basePageImpl);
				basePageImpl.setClosedPageHandler((page,stopOptions)->completeClosingPage(page,stopOptions));
				return stackViewContainer.showStartedControllerView(basePageImpl)
					.flatMap(r2->basePageImpl.doStartPage());
			});
	}
	
	private Single<Optional<Activity>> completeClosingPage(BasePageImpl closedPage,StopOptions stopOptions) {
		Preconditions.checkState(pagesStack.getLast()==closedPage);
		pagesStack.remove(closedPage);
		if (stopOptions.checkResumePage && !pagesStack.isEmpty()) {
			var toResume = pagesStack.getLast();
			return stackViewContainer.showResumedControllerView(toResume)
				.flatMap(view->toResume.doResumePage());
		}
		return Single.just(Optional.empty());
	}


	public Single<Optional<Page>> checkPausePage() {
		if (!pagesStack.isEmpty()) {
			BasePageImpl toPause = pagesStack.getLast();
			return stackViewContainer.pauseControllerView(toPause)
				.flatMap(r->Single.just(Optional.of(toPause)));
		}
		return Single.just(Optional.empty());
	}


	@Override
	public Single<Activity> startActivity(ActivityDef<?> activityDef) {
		return _getCurrentPage().startActivity(activityDef);
	}

	@Override
	public Single<Optional<Activity>> stopActivity() {
		return _getCurrentPage().stopActivity();
	}

	
	protected BasePageImpl _getCurrentPage() {
		return pagesStack.isEmpty()?null:pagesStack.getLast();
	}

	
}
