package it.fmoon.fxapp.system.configuration;

import org.springframework.stereotype.Component;

import it.fmoon.fxapp.prototype.search.SearchActivityDef;

@Component
public class ElementsActivityDef extends SearchActivityDef<ElementsActivity> {

	public ElementsActivityDef() {
		super(ElementsActivity.class);
	}
	
	
	
}