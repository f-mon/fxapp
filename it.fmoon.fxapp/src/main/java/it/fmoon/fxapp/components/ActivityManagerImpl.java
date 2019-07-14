package it.fmoon.fxapp.components;

import java.util.LinkedList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.reactivex.Single;
import it.fmoon.fxapp.controllers.application.ApplicationController;
import it.fmoon.fxapp.mvc.AbstractActivity;
import it.fmoon.fxapp.mvc.ActivityDef;

@Component
public class ActivityManagerImpl implements ActivityManager {

	private LinkedList<AbstractActivity> activityStack = new LinkedList<>();
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	ApplicationController applicationController;
	

	public Single<AbstractActivity> startActivity(ActivityDef<?> activityDef) {
		return pauseActivity()
			.flatMap((r)->{				
				AbstractActivity newActivityInstance = activityDef.newActivityInstance(applicationContext);
				activityStack.add(newActivityInstance);
				return applicationController.showStartedControllerView(newActivityInstance)
					.flatMap(r2->Single.just(newActivityInstance));
			});
	}

	public Single<Optional<AbstractActivity>> stopActivity() {
		return stopActivity(true);
	}
	
	public Single<Optional<AbstractActivity>> stopActivity(boolean checkResume) {
		if (!activityStack.isEmpty()) {
			AbstractActivity last = activityStack.removeLast();
			return applicationController.removeStoppedControllerView(last)
				.flatMap(paused -> checkResume?
						checkResumeActivity():
						Single.just(Optional.empty()))
				.flatMap(resumed ->Single.just(Optional.of(last)));
		}
		return Single.just(Optional.empty());
	}
	
	public Single<Optional<AbstractActivity>> pauseActivity() {
		if (!activityStack.isEmpty()) {
			AbstractActivity toPause = activityStack.getLast();
			return applicationController.pauseControllerView(toPause)
				.flatMap(r->Single.just(Optional.of(toPause)));
		}
		return Single.just(Optional.empty());
	}

	private Single<Optional<AbstractActivity>> checkResumeActivity() {
		if (!activityStack.isEmpty()) {
			AbstractActivity toResume = activityStack.getLast();
			return applicationController.showResumedControllerView(toResume)
				.flatMap(view ->Single.just(Optional.of(toResume)));
		}
		return Single.just(Optional.empty());
	}
	
	
	
}
