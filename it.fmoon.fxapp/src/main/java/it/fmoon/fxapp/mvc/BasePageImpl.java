package it.fmoon.fxapp.mvc;

import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.reactivex.Single;
import it.fmoon.fxapp.components.ActivityAnimator;
import it.fmoon.fxapp.controllers.application.ControllerStackViewContainer;
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

		boolean checkResume;
		public boolean checkClosePage;
		public boolean checkResumePage = true;
		
		public StopOptions(boolean checkResume,boolean checkClosePage) {
			this.checkResume = checkResume;
			this.checkClosePage = checkClosePage;
		}

	}

	protected final String id = UUID.randomUUID().toString();
	protected final PageDef pageDef;
	
	private LinkedList<AbstractActivity<?>> activityStack = new LinkedList<>();
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ActivityAnimator activityAnimator;
	
	private StackPane stackPane;
	
	private ControllerStackViewContainer stackViewContainer;
	
	
	private ActivityDef<?> initialActivity;
	private BiFunction<BasePageImpl,StopOptions,Single<Optional<Activity>>> closedPageHandler;
	private BasePageImpl parentPage;
	
	
	public BasePageImpl(PageDef pageDef) {
		this.pageDef = pageDef;
	}
	
	@PostConstruct
	public void initialize() {
		this.stackViewContainer = new ControllerStackViewContainerHelper(()->this.stackPane,this.activityAnimator);
		this.initialActivity = this.pageDef.getInitialActivity();
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
				return stackViewContainer.showStartedControllerView(aa)
					.flatMap(r2->Single.just(newActivityInstance));
			});
	}

	public Single<Optional<Activity>> stopActivity() {
		return stopActivity(new StopOptions(true,true));
	}
	
	public Single<Optional<Activity>> stopActivity(StopOptions stopOptions) {
		if (isRootPage() && isOnRootActivity()) {
			return Single.just(Optional.empty());
		}
		if (!activityStack.isEmpty()) {
			AbstractActivity<?> last = activityStack.removeLast();
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

	


}
