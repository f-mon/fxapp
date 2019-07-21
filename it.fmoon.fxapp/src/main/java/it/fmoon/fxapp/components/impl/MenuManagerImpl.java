package it.fmoon.fxapp.components.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.components.menu.MenuManager;

@Component
public class MenuManagerImpl implements MenuManager {

	private List<AppMenuItem> applicationMenu = Lists.newArrayList();
	private BehaviorSubject<List<AppMenuItem>> applicationMenuSubject = BehaviorSubject.createDefault(applicationMenu);
	
	
	@Override
	public void addToApplicationMenu(AppMenuItem menuItem) {
		applicationMenuSubject.getValue().add(menuItem);
		applicationMenuSubject.onNext(applicationMenu);
	}

	@Override
	public List<AppMenuItem> getApplicationMenus() {
		return Collections.unmodifiableList(applicationMenu);
	}

	@Override
	public Observable<List<AppMenuItem>> getApplicationMenusObs() {
		return applicationMenuSubject;
	}

}
