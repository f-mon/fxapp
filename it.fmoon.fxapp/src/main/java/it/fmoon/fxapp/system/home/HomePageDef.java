package it.fmoon.fxapp.system.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;

@Component
public class HomePageDef extends PageDef {

	@Autowired
	private HomeActivityDef homeActivity;

	@Override
	public ActivityDef<?> getInitialActivity() {
		return homeActivity;
	}

}
