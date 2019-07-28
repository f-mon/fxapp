package it.fmoon.fxapp.components.menu;

import com.google.common.base.MoreObjects;

import it.fmoon.fxapp.mvc.ActivityDef;

public class ActivityMenuItem extends AppMenuItem {
	
	protected final ActivityDef<?> activityDef;
	
	public static ActivityMenuItemBuilder builder() {
		return new ActivityMenuItemBuilder();
	}

	public ActivityMenuItem(ActivityDef<?> activityDef,String label, String icon) {
		super(label, icon);
		this.activityDef = activityDef;
	}

	public ActivityDef<?> getActivityDef() {
		return activityDef;
	}	
	
	
	public static class ActivityMenuItemBuilder extends AppMenuItem.AppMenuItemBuilder<ActivityMenuItemBuilder, ActivityMenuItem> {

		private ActivityDef<?> activityDef;
		
		public ActivityMenuItemBuilder activityDef(ActivityDef<?> activityDef) {
			this.activityDef = activityDef;
			return this;
		}
		
		@Override
		public ActivityMenuItem build() {
			return new ActivityMenuItem(activityDef,
				MoreObjects.firstNonNull(this.label,activityDef.getLabel()),
				MoreObjects.firstNonNull(this.icon,activityDef.getIcon()));
		}
		
	}
	
}
