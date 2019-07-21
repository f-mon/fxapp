package it.fmoon.fxapp.mvc;

import java.util.UUID;

import javax.annotation.PostConstruct;

public abstract class AbstractActivity<D extends ActivityDef<?>> 
	extends AbstractController 
	implements Activity 
{

	private final String id = UUID.randomUUID().toString();
	private final D activityDef;
	
	private Activity parentActivity;
	private Page page;
	
	protected String title;
	
	public AbstractActivity(D def) {
		this.activityDef = def;
	}

	@PostConstruct
	public void initializeActivity() {
		initTitle();
	}
	
	
	protected void initTitle() {
		this.title = this.activityDef.getName();
	}

	public D getActivityDef() {
		return activityDef;
	}
	
	@Override
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Activity getParentActivity() {
		return this.parentActivity;
	}
	
	public Page getPage() {
		return this.page;
	}
	

	
}
