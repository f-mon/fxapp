package it.fmoon.fxapp.support;

import java.util.function.Supplier;

import io.reactivex.Single;
import it.fmoon.fxapp.components.ActivityAnimator;
import it.fmoon.fxapp.components.ControllerStackViewContainer;
import it.fmoon.fxapp.mvc.AbstractController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class ControllerStackViewContainerHelper implements ControllerStackViewContainer {

	private final Supplier<StackPane> parentSupplier;
	private ActivityAnimator activityAnimator;

	public ControllerStackViewContainerHelper(Supplier<StackPane> parentSupplier, ActivityAnimator activityAnimator) {
		super();
		this.parentSupplier = parentSupplier;
		this.activityAnimator = activityAnimator;
	}


	public Single<Boolean> pauseControllerView(AbstractController ac) {
		Parent view = ac.getView();
		StackPane parent = this.parentSupplier.get();
		if (parent.getChildren().contains(view)) {
			return activityAnimator.removeAnimate((Node) view, parent,true)
					.flatMap(n->Single.just(Boolean.TRUE));
		}
		return Single.just(Boolean.TRUE);
	}
	
	public Single<Boolean> removeStoppedControllerView(AbstractController ac) {
		Parent view = ac.getView();
		StackPane parent = this.parentSupplier.get();
		if (parent.getChildren().contains(view)) {
			return activityAnimator.removeAnimate((Node) view, parent,false)
				.flatMap((n)->Single.just(Boolean.TRUE));
		}
		return Single.just(Boolean.TRUE);
	}


	public Single<Boolean> showStartedControllerView(AbstractController ac) {
		Parent view = ac.getView();
		StackPane parent = this.parentSupplier.get();
		if (!parent.getChildren().contains(view)) {
			return activityAnimator.insertAnimate((Node) view, parent,false)
				.flatMap((n)->Single.just(Boolean.TRUE));
		}
		return Single.just(Boolean.TRUE);
	}

	public Single<Boolean> showResumedControllerView(AbstractController ac) {
		Parent view = ac.getView();
		StackPane parent = this.parentSupplier.get();
		if (!parent.getChildren().contains(view)) {
			return activityAnimator.insertAnimate((Node) view, parent,true)
				.flatMap((n)->Single.just(Boolean.TRUE));
		}
		return Single.just(Boolean.TRUE);
	}

	
	public ActivityAnimator getActivityAnimator() {
		return activityAnimator;
	}

	public void setActivityAnimator(ActivityAnimator activityAnimator) {
		this.activityAnimator = activityAnimator;
	}
}
