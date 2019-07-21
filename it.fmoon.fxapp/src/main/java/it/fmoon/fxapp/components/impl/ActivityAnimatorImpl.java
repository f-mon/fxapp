package it.fmoon.fxapp.components.impl;

import org.springframework.stereotype.Component;

import io.reactivex.Single;
import it.fmoon.fxapp.components.ActivityAnimator;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

@Component
public class ActivityAnimatorImpl implements ActivityAnimator {


	private static final int REMOVE_ANIMATION_DURATION = 300;
	private static final int INSERT_ANUMATION_DURATION = 300;

	
	public Single<Node> removeAnimate(final Node node, final StackPane parent,boolean ispause) {
		if (parent.getChildren().contains(node)) {
			TranslateTransition transition = new TranslateTransition(Duration.millis(REMOVE_ANIMATION_DURATION), node);
//			final FadeTransition transition = new FadeTransition(Duration.millis(250), node);
			
			transition.setFromX(0);
			if (ispause) {
				transition.setToX(-parent.getWidth());
			} else {				
				transition.setToX(parent.getWidth());
			}
			
			transition.setInterpolator(Interpolator.EASE_BOTH);
			
			transition.play();
			
			return Single.create(emitter -> {
				transition.setOnFinished((e) -> {
					parent.getChildren().remove(node);
					emitter.onSuccess(node);
				});
			});
		}
		return Single.just(node);
	}

	public Single<Node> insertAnimate(final Node node, final StackPane parent, boolean isresume) {
		if (!parent.getChildren().contains(node)) {
			
			if (isresume) {
				node.setTranslateX(-parent.getWidth());
			} else {
				node.setTranslateX(parent.getWidth());
			}
			parent.getChildren().add(node);
			
			
			TranslateTransition transition = new TranslateTransition(Duration.millis(INSERT_ANUMATION_DURATION), node);
			if (isresume) {
				transition.setFromX(-parent.getWidth());
			} else {				
				transition.setFromX(parent.getWidth());
			}
			transition.setToX(0);
			transition.setInterpolator(Interpolator.EASE_BOTH);
			
			transition.play();
			
			return Single.create(emitter -> {
				transition.setOnFinished((e) -> {
					emitter.onSuccess(node);
				});
			});
		}
		return Single.just(node);
	}

}
