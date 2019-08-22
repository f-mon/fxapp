package it.fmoon.fxapp.mvc;

import java.util.List;

import io.reactivex.Observable;
import it.fmoon.fxapp.common.HasId;
import it.fmoon.fxapp.components.menu.AppMenuItem;

public interface Page extends HasId {
	
	PageDef getPageDef();
	
	default String getName() {
		return getPageDef().getName();
	}

	String getTitle();
	
	boolean isRootPage();

	Page getParentPage();

	Activity getCurrentActivity();
	
	List<Activity> getActivityStack();

	Observable<List<AppMenuItem>> getPageMenuObs();

	AbstractController getHeaderController();


}
