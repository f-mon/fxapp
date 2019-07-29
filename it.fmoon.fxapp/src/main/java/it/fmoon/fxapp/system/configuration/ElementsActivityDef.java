package it.fmoon.fxapp.system.configuration;

import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.ActivityDef;

@Component
public class ElementsActivityDef extends ActivityDef<ElementsActivity> {

	public ElementsActivityDef() {
		super(ElementsActivity.class);
	}
	
}