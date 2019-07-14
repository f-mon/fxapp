package it.fmoon.fxapp.controllers.application;

import io.reactivex.Single;
import it.fmoon.fxapp.mvc.AbstractController;

public interface ControllerStackViewContainer {
	
	public Single<Boolean> pauseControllerView(AbstractController ac);
	
	public Single<Boolean> removeStoppedControllerView(AbstractController ac);

	public Single<Boolean> showStartedControllerView(AbstractController ac);

	public Single<Boolean> showResumedControllerView(AbstractController ac);
}
