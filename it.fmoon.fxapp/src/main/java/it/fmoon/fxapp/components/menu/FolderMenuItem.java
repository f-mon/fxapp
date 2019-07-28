package it.fmoon.fxapp.components.menu;

import java.util.List;

import com.google.common.collect.Lists;

public class FolderMenuItem extends AppMenuItem {

	private final List<AppMenuItem> menu = Lists.newArrayList();
	
	public static FolderMenuItemBuilder builder() {
		return new FolderMenuItemBuilder();
	}
	
	public FolderMenuItem(List<AppMenuItem> menu, String label, String icon) {
		super(label, icon);
		this.menu.addAll(menu);
	}

	public List<AppMenuItem> getMenu() {
		return menu;
	}
	
	public static class FolderMenuItemBuilder extends AppMenuItem.AppMenuItemBuilder<FolderMenuItemBuilder, FolderMenuItem> {

		private List<AppMenuItem> menu = Lists.newArrayList();
		
		public FolderMenuItemBuilder item(AppMenuItem menuItem) {
			this.menu.add(menuItem);
			return this;
		}
		
		@Override
		public FolderMenuItem build() {
			return new FolderMenuItem(menu,this.label,this.icon);
		}
		
	}
	

	
}
