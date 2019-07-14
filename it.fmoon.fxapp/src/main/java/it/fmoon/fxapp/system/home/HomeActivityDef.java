package it.fmoon.fxapp.system.home;

import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.ActivityDef;

@Component
public class HomeActivityDef extends ActivityDef<HomeActivity> {

	public HomeActivityDef() {
		super(HomeActivity.class);
	}

	@Override
	public String getName() {
		return "homeActivity";
	}

	
}
