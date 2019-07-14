package it.fmoon.fxapp.mvc;

import it.fmoon.fxapp.common.HasId;

public interface Page extends HasId {
	
	PageDef getPageDef();
	
	default String getName() {
		return getPageDef().getName();
	}

}
