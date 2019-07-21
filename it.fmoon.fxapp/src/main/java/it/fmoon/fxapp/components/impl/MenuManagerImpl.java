package it.fmoon.fxapp.components.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.components.menu.MenuManager;

@Component
public class MenuManagerImpl implements MenuManager {

	@Autowired
	private ActivityManager activityManager;
	
	private List<AppMenuItem> applicationMenu = Lists.newArrayList();
	private BehaviorSubject<List<AppMenuItem>> applicationMenuSubject = BehaviorSubject.createDefault(Collections.unmodifiableList(applicationMenu));
	
	
	@Override
	public void addToApplicationMenu(AppMenuItem menuItem) {
		applicationMenu.add(menuItem);
		applicationMenuSubject.onNext(Collections.unmodifiableList(applicationMenu));
	}

	@Override
	public List<AppMenuItem> getApplicationMenus() {
		return Collections.unmodifiableList(applicationMenu);
	}

	@Override
	public Observable<List<AppMenuItem>> getApplicationMenusObs() {
		return applicationMenuSubject;
	}

	@Override
	public Observable<List<AppMenuItem>> getCurrentPageMenusObs() {
		return activityManager.onCurrentPage()
			.switchMap(p->p.getPageMenuObs());
	}

}
