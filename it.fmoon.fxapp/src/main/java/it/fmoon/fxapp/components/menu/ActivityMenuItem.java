package it.fmoon.fxapp.components.menu;

import it.fmoon.fxapp.mvc.ActivityDef;

public class ActivityMenuItem extends AppMenuItem {
	
	protected ActivityDef<?> activityDef;

	public ActivityMenuItem(ActivityDef<?> activityDef) {
		this.activityDef = activityDef;
	}

	public ActivityDef<?> getActivityDef() {
		return activityDef;
	}	
	
}
