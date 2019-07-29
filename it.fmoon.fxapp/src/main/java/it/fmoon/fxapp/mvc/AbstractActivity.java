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
	private String icon;
	
	public AbstractActivity(D def) {
		this.activityDef = def;
	}

	@PostConstruct
	public void initializeActivity() {
		initTitle();
		initIcon();
	}
	
	protected void initIcon() {
		this.icon = this.activityDef.getIcon();
	}

	protected void initTitle() {
		this.title = this.activityDef.getLabel();
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
	
	public String getIcon() {
		return icon;
	}

	public Activity getParentActivity() {
		return this.parentActivity;
	}
	
	public Page getPage() {
		return this.page;
	}
	

	
}
