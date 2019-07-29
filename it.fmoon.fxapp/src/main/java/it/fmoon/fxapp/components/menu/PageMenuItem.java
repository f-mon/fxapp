package it.fmoon.fxapp.components.menu;

import com.google.common.base.MoreObjects;

import it.fmoon.fxapp.mvc.PageDef;

public class PageMenuItem extends AppMenuItem {
	
	protected final PageDef pageDef;
	
	public static PageMenuItemBuilder builder() {
		return new PageMenuItemBuilder();
	}

	public PageMenuItem(PageDef pageDef,String label, String icon) {
		super(label, icon);
		this.pageDef = pageDef;
	}

	public PageDef getPageDef() {
		return pageDef;
	}	
	
	public static class PageMenuItemBuilder extends AppMenuItem.AppMenuItemBuilder<PageMenuItemBuilder, PageMenuItem> {

		private PageDef pageDef;
		
		public PageMenuItemBuilder pageDef(PageDef pageDef) {
			this.pageDef = pageDef;
			return this;
		}
		
		@Override
		public PageMenuItem build() {
			return new PageMenuItem(pageDef,
				MoreObjects.firstNonNull(this.label,pageDef.getLabel()),
				MoreObjects.firstNonNull(this.icon,pageDef.getIcon()));
		}
		
	}

}
