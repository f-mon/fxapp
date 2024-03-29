package it.fmoon.fxapp.components.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.components.ControllerStackViewContainer;
import it.fmoon.fxapp.mvc.Activity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.BasePageImpl;
import it.fmoon.fxapp.mvc.BasePageImpl.StopOptions;
import it.fmoon.fxapp.mvc.Page;
import it.fmoon.fxapp.mvc.PageDef;

@Component
public class ActivityManagerImpl implements ActivityManager {

	private LinkedList<BasePageImpl> pagesStack = new LinkedList<>();
	private BehaviorSubject<List<Page>> navigationStack = BehaviorSubject.createDefault(Collections.unmodifiableList(pagesStack));
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	ControllerStackViewContainer stackViewContainer;
	
	private PageDef applicationRootPage;
	private ActivityDef<?> applicationRootActivity;
	
	
	@Override
	public Single<Page> startApplication() {
		return startPage(applicationRootPage);
	}
	
	@Override
	public Single<Page> startPage(PageDef pageDef) {
		return checkPausePage()
			.flatMap((r)->{				
				Page newPageInstance = pageDef.newPageInstance(applicationContext);
				BasePageImpl basePageImpl = (BasePageImpl)newPageInstance;
				basePageImpl.setParentPage(pagesStack.peekLast());
				pagesStack.add(basePageImpl);
				
				basePageImpl.onActivityStack().subscribe(actList->this.notifyNavStack());
				notifyNavStack();
				
				basePageImpl.setClosedPageHandler((page,stopOptions)->completeClosingPage(page,stopOptions));
				
				return stackViewContainer.showStartedControllerView(basePageImpl)
					.flatMap(r2->basePageImpl.doStartPage());
			});
	}
	
	@Override
	public Single<Page> startRootPage(PageDef pageDef) {
		return stopAllPages()
			.flatMap(page->startPage(pageDef));
	}

	private Single<Boolean> stopAllPages() {
		if (pagesStack.isEmpty()) {
			return Single.just(Boolean.TRUE);
		}
		return stopPage(new StopOptions(true,true,false,false))
			.flatMap(opt->Single.just(Boolean.TRUE));
	}

	private Single<Optional<Page>> stopPage(StopOptions stopOptions) {
		if (!pagesStack.isEmpty()) {
			BasePageImpl toStop = pagesStack.getLast();
			return toStop.doStopPage(stopOptions);
		}
		return Single.just(Optional.empty());
	}

	private Single<Optional<Activity>> completeClosingPage(BasePageImpl closedPage,StopOptions stopOptions) {
		Preconditions.checkState(pagesStack.getLast()==closedPage);
		pagesStack.remove(closedPage);
		notifyNavStack();
		if (stopOptions.isCheckResumePage() && !pagesStack.isEmpty()) {
			var toResume = pagesStack.getLast();
			return stackViewContainer.showResumedControllerView(toResume)
				.flatMap(view->toResume.doResumePage());
		}
		else if (stopOptions.isCheckRestartAppRootPage() && pagesStack.isEmpty()) {
			return startApplication()
				.flatMap(p->Single.just(Optional.of(p.getCurrentActivity())));
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
	public Single<Activity> startRootPageActivity(ActivityDef<?> activityDef) {
		return _getCurrentPage().startRootActivity(activityDef);
	}

	@Override
	public Single<Optional<Activity>> stopActivity() {
		return _getCurrentPage().stopActivity();
	}

	
	protected BasePageImpl _getCurrentPage() {
		return pagesStack.isEmpty()?null:pagesStack.getLast();
	}

	@Override
	public Observable<List<Page>> onNavigationStack() {
		return navigationStack;
	}

	private void notifyNavStack() {
		System.out.println("fire notify Act Stack");
		this.navigationStack.onNext(Collections.unmodifiableList(pagesStack));
	}

	@Override
	public Observable<Page> onCurrentPage() {
		return this.navigationStack
			.map(navStack->navStack.isEmpty()?
					Optional.<Page>empty():
					Optional.of(navStack.get(navStack.size()-1)))
			.filter(opt->opt.isPresent())
			.map(opt->opt.get())
			.distinctUntilChanged();
	}
	
	@Override
	public Observable<Activity> onCurrentActivity() {
		return this.navigationStack
			.map(navStack->navStack.isEmpty()?
					Optional.<Page>empty():
					Optional.of(navStack.get(navStack.size()-1)))
			.filter(opt->opt.isPresent())
			.map(opt->Optional.ofNullable(opt.get().getCurrentActivity()))
			.filter(opt->opt.isPresent())
			.map(opt->opt.get())
			.distinctUntilChanged();
	}

	@Override
	public void registerApplicationRootPageAndActivity(PageDef appRootPage, ActivityDef<?> appRootActivity) {
		this.applicationRootPage = appRootPage;
		this.applicationRootActivity = appRootActivity;
	}

	@Override
	public boolean isApplicationRootPage(PageDef pageDef) {
		return this.applicationRootPage.getName().equals(pageDef.getName());
	}

	@Override
	public boolean isApplicationRootActivity(ActivityDef<?> activityDef) {
		return this.applicationRootActivity.getName().equals(activityDef.getName());
	}


	
}
