package it.fmoon.fxapp.components;

import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.Single;
import it.fmoon.fxapp.mvc.Activity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.Page;
import it.fmoon.fxapp.mvc.PageDef;

public interface ActivityManager {

	
	public Single<Page> startPage(PageDef pageDef);
	
	public Single<Activity> startActivity(ActivityDef<?> activityDef);

	public Single<Optional<Activity>> stopActivity();

	public Observable<List<Page>> onNavigationStack();

	public Observable<Page> onCurrentPage();
	
	
}
