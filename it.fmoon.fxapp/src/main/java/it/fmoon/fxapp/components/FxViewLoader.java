package it.fmoon.fxapp.components;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.support.FxViewFactory;
import javafx.scene.Node;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FxViewLoader {

	private FxLoader fxLoader;
	private Node view;

	public FxViewLoader(FxLoader fxLoader) {
		super();
		this.fxLoader = fxLoader;
	}

	public Node get(Object controller) {
		if (this.view==null) {
			if (controller instanceof FxViewFactory) {
				this.view = ((FxViewFactory) controller).createView();
			} else {				
				this.view = this.fxLoader.loadView(controller);
			}
		}
		return this.view;
	}
	
	public FxLoader loader() {
		return this.fxLoader;
	}
	
	
}
