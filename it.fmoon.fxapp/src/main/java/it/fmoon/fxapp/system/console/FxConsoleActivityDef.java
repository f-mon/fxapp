package it.fmoon.fxapp.system.console;

import org.springframework.stereotype.Component;

import it.fmoon.fxapp.prototype.search.SearchActivityDef;

@Component
public class FxConsoleActivityDef extends SearchActivityDef<FxConsoleActivity> {

	public FxConsoleActivityDef() {
		super(FxConsoleActivity.class);
	}
	
	
	
}