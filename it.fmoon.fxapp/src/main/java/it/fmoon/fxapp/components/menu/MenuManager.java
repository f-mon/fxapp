package it.fmoon.fxapp.components.menu;

import java.util.List;

import io.reactivex.Observable;

public interface MenuManager {

	void addToApplicationMenu(AppMenuItem menuItem);

	List<AppMenuItem> getApplicationMenus();
	
	Observable<List<AppMenuItem>> getApplicationMenusObs();

	Observable<List<AppMenuItem>> getCurrentPageMenusObs();
	
}
