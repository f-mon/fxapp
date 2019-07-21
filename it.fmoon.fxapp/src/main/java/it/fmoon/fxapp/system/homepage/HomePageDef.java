package it.fmoon.fxapp.system.homepage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
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
	protected void initPageMenu(List<AppMenuItem> pageMenuDefinition) {
		pageMenuDefinition.add(
			ActivityMenuItem.builder()
				.activityDef(homeActivity)
				.label("Home Activity")
				.icon("icona")
				.build()
		);
		pageMenuDefinition.add(
			ActivityMenuItem.builder()
				.activityDef(homeActivity)
				.label("Login Activity")
				.icon("icona")
				.build()
			);
	}

	@Override
	public ActivityDef<?> getInitialActivity() {
		return homeActivity;
	}

}
