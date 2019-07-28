package it.fmoon.fxapp.system;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.menu.MenuManager;
import it.fmoon.fxapp.components.menu.PageMenuItem;
import it.fmoon.fxapp.system.configuration.ConfigurationPageDef;

@Component
public class SystemModule {

	@Autowired
	private MenuManager menuManager;
	
	@Autowired
	private ConfigurationPageDef configurationPageDef;
	
	@PostConstruct
	public void initializeModule() {
		menuManager.addToApplicationMenu(new PageMenuItem(configurationPageDef, "Configuration", "fa-cog"));
	}
	
}
