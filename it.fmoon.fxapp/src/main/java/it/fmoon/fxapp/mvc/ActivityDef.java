package it.fmoon.fxapp.mvc;

import org.springframework.context.ApplicationContext;

public abstract class ActivityDef<A extends Activity> {

	protected final Class<A> activityType;
	
	public ActivityDef(Class<A> activityType) {
		super();
		this.activityType = activityType;
	}

	abstract public String getName();
	
	public A newActivityInstance(ApplicationContext applicationContext) {
		return applicationContext.getBean(this.activityType,this);
	}
	
}
