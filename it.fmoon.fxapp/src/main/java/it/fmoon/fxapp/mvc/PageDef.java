package it.fmoon.fxapp.mvc;

import org.springframework.context.ApplicationContext;

public abstract class PageDef {

	protected final Class<? extends Page> pageType;
	
	public PageDef() {
		this(BasePageImpl.class);
	}
	public PageDef(Class<? extends Page> pageType) {
		this.pageType = pageType;
	}

	abstract public String getName();
	
	public Page newPageInstance(ApplicationContext applicationContext) {
		return applicationContext.getBean(this.pageType,this);
	}
}
