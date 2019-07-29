package it.fmoon.fxapp.system.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.mvc.PageDef;

@Component
public class ConfigurationPageDef extends PageDef {

	@Autowired
	private ElementsActivityDef elementActivityDef;
	
	@Override
	public String getName() {
		return "configuration";
	}
	
	@Override
	protected void definePageMenu(List<AppMenuItem> pageMenuDefinition) {
		pageMenuDefinition.add(
			ActivityMenuItem.builder()
				.activityDef(elementActivityDef)
				.build()
		);
	}

}
