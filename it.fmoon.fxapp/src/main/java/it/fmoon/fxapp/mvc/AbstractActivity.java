package it.fmoon.fxapp.mvc;

import java.util.UUID;

public abstract class AbstractActivity<D extends ActivityDef<?>> 
	extends AbstractController 
	implements Activity 
{

	private final String id = UUID.randomUUID().toString();
	private final D activityDef;
	
	private Activity parentActivity;
	private Page page;
	
	
	public AbstractActivity(D def) {
		this.activityDef = def;
	}

	public D getActivityDef() {
		return activityDef;
	}
	
	@Override
	public String getId() {
		return id;
	}

	public Activity getParentActivity() {
		return this.parentActivity;
	}
	
	public Page getPage() {
		return this.page;
	}
	
}
