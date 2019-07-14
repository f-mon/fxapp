package it.fmoon.fxapp.system.homepage;

import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.PageDef;

@Component
public class HomePageDef extends PageDef {

	@Override
	public String getName() {
		return "homepage";
	}

}
