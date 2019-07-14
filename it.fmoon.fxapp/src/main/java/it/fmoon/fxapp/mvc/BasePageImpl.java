package it.fmoon.fxapp.mvc;

import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

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

	protected final String id = UUID.randomUUID().toString();
	protected final PageDef pageDef;
	
	private LinkedList<AbstractActivity<?>> activityStack = new LinkedList<>();
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ActivityAnimator activityAnimator;
	
	private StackPane stackPane;
	
	private ControllerStackViewContainer stackViewContainer;
	
	
	public BasePageImpl(PageDef pageDef) {
		this.pageDef = pageDef;
	}
	
	@PostConstruct
	public void initialize() {
		stackViewContainer = new ControllerStackViewContainerHelper(()->this.stackPane,this.activityAnimator);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public PageDef getPageDef() {
		return pageDef;
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
		return stopActivity(true);
	}
	
	public Single<Optional<Activity>> stopActivity(boolean checkResume) {
		if (!activityStack.isEmpty()) {
			AbstractActivity<?> last = activityStack.removeLast();
			return stackViewContainer.removeStoppedControllerView(last)
				.flatMap(paused -> checkResume?
						checkResumeActivity():
						Single.just(Optional.empty()))
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

}
