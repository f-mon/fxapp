package it.fmoon.fxapp.system.homepage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;
import it.fmoon.fxapp.system.home.HomeActivityDef;

@Component
public class HomePageDef extends PageDef {

	@Autowired
	private HomeActivityDef homeActivity;
	
	@Override
	public String getName() {
		return "homepage";
	}

	@Override
	public ActivityDef<?> getInitialActivity() {
		return homeActivity;
	}

}
