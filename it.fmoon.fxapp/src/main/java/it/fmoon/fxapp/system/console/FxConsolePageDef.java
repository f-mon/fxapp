package it.fmoon.fxapp.system.console;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.mvc.PageDef;
import it.fmoon.fxapp.system.console.header.FxCommanderController;

@Component
public class FxConsolePageDef extends PageDef {

	@Autowired
	private FxConsoleActivityDef fxConsoleActivityDef;
	
	@Override
	public String getName() {
		return "console";
	}
	
	@Override
	protected void definePageMenu(List<AppMenuItem> pageMenuDefinition) {
		pageMenuDefinition.add(
			ActivityMenuItem.builder()
				.activityDef(fxConsoleActivityDef)
				.build()
		);
	}
	
	@Override
	public Class<? extends AbstractController> getPageHeaderController() {
		return FxCommanderController.class;
	}

}
