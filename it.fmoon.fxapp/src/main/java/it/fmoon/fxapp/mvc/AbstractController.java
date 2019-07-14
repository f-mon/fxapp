package it.fmoon.fxapp.mvc;

import org.springframework.beans.factory.annotation.Autowired;

import it.fmoon.fxapp.components.FxViewLoader;
import javafx.scene.Parent;

public class AbstractController {
	
	@Autowired
	private FxViewLoader viewLoader;

	public AbstractController() {
		super();
	}
	public AbstractController(FxViewLoader viewLoader) {
		super();
		this.viewLoader = viewLoader;
	}

	public Parent getView() {
		return this.viewLoader.get(this);
	}
	
}
