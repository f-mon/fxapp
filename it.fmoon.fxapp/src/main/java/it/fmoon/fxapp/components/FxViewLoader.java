package it.fmoon.fxapp.components;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import it.fmoon.fxapp.support.FxViewFactory;
import javafx.scene.Node;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FxViewLoader {

	private FxLoader fxLoader;
	private BehaviorSubject<Node> viewSubj = BehaviorSubject.create();

	private AtomicBoolean building = new AtomicBoolean(false);

	public FxViewLoader(FxLoader fxLoader) {
		super();
		this.fxLoader = fxLoader;
	}

	public Node get(Object controller) {
		if (!this.viewSubj.hasValue()) {
			if (!building.getAndSet(true)) {
				buildView(controller);
			} else {
				throw new IllegalStateException("FxViewLoader get method called while in building view");
			}
		}
		return this.viewSubj.getValue();
	}
	
	protected void buildView(Object controller) {
		Node view;
		if (controller instanceof FxViewFactory) {
			view = ((FxViewFactory) controller).createView();
		} else {				
			view = this.fxLoader.loadView(controller);
		}
		this.viewSubj.onNext(view);
		building.set(false);
	}

	public FxLoader loader() {
		return this.fxLoader;
	}
	
	public Observable<Node> onView() {
		return this.viewSubj;
	} 
}
