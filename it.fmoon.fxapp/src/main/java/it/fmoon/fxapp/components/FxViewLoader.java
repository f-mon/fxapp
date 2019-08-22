package it.fmoon.fxapp.components;

import java.util.concurrent.atomic.AtomicBoolean;

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

	private AtomicBoolean building = new AtomicBoolean(false);

	public FxViewLoader(FxLoader fxLoader) {
		super();
		this.fxLoader = fxLoader;
	}

	public Node get(Object controller) {
		if (this.view==null) {
			if (!building.getAndSet(true)) {
				buildView(controller);
			} else {
				throw new IllegalStateException("FxViewLoader get method called while in building view");
			}
		}
		return this.view;
	}
	
	protected void buildView(Object controller) {
		if (controller instanceof FxViewFactory) {
			this.view = ((FxViewFactory) controller).createView();
		} else {				
			this.view = this.fxLoader.loadView(controller);
		}
		building.set(false);
	}

	public FxLoader loader() {
		return this.fxLoader;
	}
	
	
}
