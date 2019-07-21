package it.fmoon.fxapp.mvc;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import it.fmoon.fxapp.components.ActivityAnimator;
import it.fmoon.fxapp.components.ControllerStackViewContainer;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.support.ControllerStackViewContainerHelper;
import it.fmoon.fxapp.support.FxViewFactory;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BasePageImpl
	extends AbstractController
	implements Page,FxViewFactory
{

	public static class StopOptions {

		private boolean checkResume;
		private boolean checkClosePage;
		private boolean preventCloseRootActivity;
		private boolean checkResumePage = true;
		
		public StopOptions(boolean checkResume,boolean checkClosePage) {
			this(checkResume,checkClosePage,true);
		}
		public StopOptions(boolean checkResume,boolean checkClosePage,boolean preventCloseRootActivity) {
			this.checkResume = checkResume;
			this.checkClosePage = checkClosePage;
			this.preventCloseRootActivity = preventCloseRootActivity;
		}

		public boolean isPreventCloseRootActivity() {
			return this.preventCloseRootActivity;
		}
		public boolean isCheckResumePage() {
			return this.checkResumePage;
		}

	}

	protected final String id = UUID.randomUUID().toString();
	protected final PageDef pageDef;
	
	private LinkedList<AbstractActivity<?>> activityStack = new LinkedList<>();
	private BehaviorSubject<List<Activity>> activityStackObs = BehaviorSubject.createDefault(Collections.unmodifiableList(activityStack));
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ActivityAnimator activityAnimator;
	
	private StackPane stackPane;
	
	private ControllerStackViewContainer stackViewContainer;
	
	private List<AppMenuItem> pageMenu = Lists.newArrayList();
	private BehaviorSubject<List<AppMenuItem>> pageMenuSubject = BehaviorSubject.createDefault(pageMenu);
	
	
	private ActivityDef<?> initialActivity;
	private BiFunction<BasePageImpl,StopOptions,Single<Optional<Activity>>> closedPageHandler;
	private BasePageImpl parentPage;
	private String title;
	
	
	public BasePageImpl(PageDef pageDef) {
		this.pageDef = pageDef;
	}
	
	@PostConstruct
	public void initializePage() {
		this.stackViewContainer = new ControllerStackViewContainerHelper(()->this.stackPane,this.activityAnimator);
		this.initialActivity = this.pageDef.getInitialActivity();
		initTitle();
		initPageMenu();
	}

	protected void initPageMenu() {
		this.pageMenu.clear();
		this.pageMenu.addAll(this.pageDef.getPageMenu());
	}

	protected void initTitle() {
		this.title = this.pageDef.getName();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public PageDef getPageDef() {
		return pageDef;
	}
	
	@Override
	public boolean isRootPage() {
		return getParentPage()==null;
	}

	public boolean isOnRootActivity() {
		return activityStack.size()==1;
	}
	
	@Override
	public Activity getCurrentActivity() {
		return activityStack.peekLast();
	}

	@Override
	public List<Activity> getActivityStack() {
		return Collections.unmodifiableList(this.activityStack);
	}
	
	@Override
	public BasePageImpl getParentPage() {
		return this.parentPage;
	}
	public void setParentPage(BasePageImpl parentPage) {
		this.parentPage = parentPage;
	}
	
	
	public Single<Page> doStartPage() {
		ActivityDef<?> initialActivity = getInitialActivityDef();
		return startActivity(initialActivity)
			.flatMap(act->Single.just(this));
	}
	
	public Single<Optional<Activity>> doResumePage() {
		return checkResumeActivity();
	}
	
	protected ActivityDef<?> getInitialActivityDef() {
		return this.initialActivity;
	}
	
	public Single<Activity> startActivity(ActivityDef<?> activityDef) {
		return checkPauseActivity()
			.flatMap((r)->{				
				Activity newActivityInstance = activityDef.newActivityInstance(applicationContext);
				AbstractActivity<?> aa = (AbstractActivity<?>)newActivityInstance;
				activityStack.add(aa);
				notifyActivityStack();
				return stackViewContainer.showStartedControllerView(aa)
					.flatMap(r2->Single.just(newActivityInstance));
			});
	}
	
	public Single<Activity> startRootActivity(ActivityDef<?> activityDef) {
		return stopAllActivities()
			.flatMap(page->startActivity(activityDef));
	}

	public Single<Page> stopAllActivities() {
		if (activityStack.isEmpty()) {
			return Single.just(this);
		}
		return stopActivity(new StopOptions(true,false,false))
			.flatMap(stopped->stopAllActivities());
	}
	
	public Single<Optional<Activity>> stopActivity() {
		return stopActivity(new StopOptions(true,true));
	}
	
	public Single<Optional<Activity>> stopActivity(StopOptions stopOptions) {
		if (stopOptions.isPreventCloseRootActivity() && isRootPage() && isOnRootActivity()) {
			return Single.just(Optional.empty());
		}
		if (!activityStack.isEmpty()) {
			AbstractActivity<?> last = activityStack.removeLast();
			notifyActivityStack();
			return stackViewContainer.removeStoppedControllerView(last)
				.flatMap(stopped -> afterStoppedActivity(last,stopOptions))
				.flatMap(resumed ->Single.just(Optional.of(last)));
		}
		return Single.just(Optional.empty());
	}

	public Single<Optional<Activity>> checkPauseActivity() {
		if (!activityStack.isEmpty()) {
			AbstractActivity<?> toPause = activityStack.getLast();
			return stackViewContainer.pauseControllerView(toPause)
				.flatMap(r->Single.just(Optional.of(toPause)));
		}
		return Single.just(Optional.empty());
	}

	private Single<Optional<Activity>> afterStoppedActivity(AbstractActivity<?> stopped, StopOptions stopOptions) {
		if (stopOptions.checkResume && !activityStack.isEmpty()) {
			return checkResumeActivity();
		}
		if (stopOptions.checkClosePage && activityStack.isEmpty()) {
			return this.closedPageHandler.apply(this,stopOptions);
		}
		return Single.just(Optional.empty());
	}
	
	private Single<Optional<Activity>> checkResumeActivity() {
		if (!activityStack.isEmpty()) {
			AbstractActivity<?> toResume = activityStack.getLast();
			return stackViewContainer.showResumedControllerView(toResume)
				.flatMap(view ->Single.just(Optional.of(toResume)));
		}
		return Single.just(Optional.empty());
	}

	@Override
	public Parent createView() {
		this.stackPane = new StackPane();
		return stackPane;
	}

	public void setClosedPageHandler(BiFunction<BasePageImpl,StopOptions,Single<Optional<Activity>>> handler) {
		this.closedPageHandler = handler;
	}

	public Observable<List<Activity>> onActivityStack() {
		return activityStackObs;
	}
	
	private void notifyActivityStack() {
		this.activityStackObs.onNext(Collections.unmodifiableList(this.activityStack));
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Observable<List<AppMenuItem>> getPageMenuObs() {
		return this.pageMenuSubject;
	}


}
