package it.fmoon.fxapp.mvc;

import org.springframework.beans.factory.annotation.Autowired;

import io.reactivex.Observable;
import it.fmoon.fxapp.components.FxViewLoader;
import javafx.scene.Node;

public class AbstractController {
	
	@Autowired
	protected FxViewLoader viewLoader;

	public AbstractController() {
		super();
	}
	public AbstractController(FxViewLoader viewLoader) {
		super();
		this.viewLoader = viewLoader;
	}

	public Node getView() {
		return this.viewLoader.get(this);
	}
	public Observable<Node> onView() {
		return this.viewLoader.onView();
	}
	
}
