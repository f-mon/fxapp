package it.fmoon.fxapp.components;

import java.util.LinkedList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.reactivex.Single;
import it.fmoon.fxapp.controllers.application.ControllerStackViewContainer;
import it.fmoon.fxapp.mvc.Activity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.BasePageImpl;
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
				pagesStack.add(basePageImpl);
				return stackViewContainer.showStartedControllerView(basePageImpl)
					.flatMap(r2->Single.just(newPageInstance));
			});
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
