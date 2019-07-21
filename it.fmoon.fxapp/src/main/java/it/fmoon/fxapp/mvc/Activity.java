package it.fmoon.fxapp.mvc;

import it.fmoon.fxapp.common.HasId;

public interface Activity extends HasId {
	
	ActivityDef<?> getActivityDef();
	
	default String getName() {
		return getActivityDef().getName();
	}
	
	String getTitle();
	
	Activity getParentActivity();
	
	Page getPage();
	
}