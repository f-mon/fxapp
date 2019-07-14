package it.fmoon.fxapp.controllers.home;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.ActivityDef;

@Component
public class HomeActivityDef extends ActivityDef<HomeActivity> {

	@Override
	public String getName() {
		return "homeActivity";
	}
	
	public HomeActivity newActivityInstance(ApplicationContext applicationContext) {
		return applicationContext.getBean(HomeActivity.class,this);
	}

	
}
