package it.fmoon.fxapp.components;

import java.util.Optional;

import io.reactivex.Single;
import it.fmoon.fxapp.mvc.Activity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.Page;
import it.fmoon.fxapp.mvc.PageDef;

public interface ActivityManager {

	
	public Single<Page> startPage(PageDef pageDef);
	
	public Single<Activity> startActivity(ActivityDef<?> activityDef);

	public Single<Optional<Activity>> stopActivity();
	
	
}
