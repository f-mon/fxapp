package it.fmoon.fxapp.components.menu;

import java.util.List;
import java.util.Optional;

import it.fmoon.fxapp.components.menu.ActivityMenuItem.ActivityMenuItemBuilder;
import it.fmoon.fxapp.components.menu.FolderMenuItem.FolderMenuItemBuilder;
import it.fmoon.fxapp.components.menu.PageMenuItem.PageMenuItemBuilder;

public abstract class AppMenus {

	public static ActivityMenuItemBuilder newActivityMenuItem() {
		return ActivityMenuItem.builder();
	}
	public static PageMenuItemBuilder newPageMenuItem() {
		return PageMenuItem.builder();
	}
	public static FolderMenuItemBuilder newFolderMenuItem() {
		return FolderMenuItem.builder();
	}
	
	public static Optional<ActivityMenuItem> findFirstActivity(List<AppMenuItem> menu) {
		for (AppMenuItem appMenuItem: menu) {
			if (appMenuItem instanceof ActivityMenuItem) {
				return Optional.of((ActivityMenuItem)appMenuItem);
			}
			if (appMenuItem instanceof FolderMenuItem) {
				Optional<ActivityMenuItem> item = findFirstActivity(((FolderMenuItem)appMenuItem).getMenu());
				if (item.isPresent()) {
					return item;
				}
			}
		}
		return Optional.empty();
	}
	
}
