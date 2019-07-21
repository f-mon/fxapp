package it.fmoon.fxapp.components.menu;

import it.fmoon.fxapp.mvc.PageDef;

public class PageMenuItem extends AppMenuItem {
	
	protected PageDef pageDef;

	public PageMenuItem(PageDef pageDef) {
		this.pageDef = pageDef;
	}

	public PageDef getPageDef() {
		return pageDef;
	}	
	
}
