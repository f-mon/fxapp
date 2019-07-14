package it.fmoon.fxapp.components;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.support.FxViewFactory;
import javafx.scene.Parent;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FxViewLoader {

	private FxLoader fxLoader;
	private Parent view;

	public FxViewLoader(FxLoader fxLoader) {
		super();
		this.fxLoader = fxLoader;
	}

	public Parent get(Object controller) {
		if (this.view==null) {
			if (controller instanceof FxViewFactory) {
				this.view = ((FxViewFactory) controller).createView();
			} else {				
				this.view = this.fxLoader.loadView(controller);
			}
		}
		return this.view;
	}
	
	
	
}
