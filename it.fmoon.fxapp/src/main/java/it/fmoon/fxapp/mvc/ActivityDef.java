package it.fmoon.fxapp.mvc;

import org.springframework.context.ApplicationContext;

public abstract class ActivityDef<A extends Activity> {

	protected final Class<A> activityType;
	
	public ActivityDef(Class<A> activityType) {
		super();
		this.activityType = activityType;
	}

	public String getName() {
		return this.activityType.getSimpleName();
	}
	
	public String getIcon() {
		return "fa-window-maximize";
	}
	
	public String getLabel() {
		return getName();
	}
	
	public A newActivityInstance(ApplicationContext applicationContext) {
		return applicationContext.getBean(this.activityType,this);
	}
	
	/*
	 * If true the AppMenu is hidden when activity is shown 
	 */
	public boolean isFullScreen() {
		return false;
	}
	
}
