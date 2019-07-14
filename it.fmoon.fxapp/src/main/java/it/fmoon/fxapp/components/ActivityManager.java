package it.fmoon.fxapp.components;

import java.util.Optional;

import io.reactivex.Single;
import it.fmoon.fxapp.mvc.AbstractActivity;
import it.fmoon.fxapp.mvc.ActivityDef;

public interface ActivityManager {

	public Single<AbstractActivity> startActivity(ActivityDef<?> activityDef);

	public Single<Optional<AbstractActivity>> stopActivity();
	
	
}
