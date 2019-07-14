package it.fmoon.fxapp.components;

import io.reactivex.Single;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public interface ActivityAnimator {
	
	public Single<Node> insertAnimate(final Node node, final StackPane parent, boolean isresume);
	
	public Single<Node> removeAnimate(final Node node, final StackPane parent,boolean ispause);

}
