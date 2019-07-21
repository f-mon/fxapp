package it.fmoon.fxapp.mvc;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;

import it.fmoon.fxapp.components.menu.AppMenuItem;

public abstract class PageDef {

	protected final Class<? extends Page> pageType;
	protected final List<AppMenuItem> pageMenuDefinition;
	
	public PageDef() {
		this(BasePageImpl.class);
	}
	public PageDef(Class<? extends Page> pageType) {
		this.pageType = pageType;
		this.pageMenuDefinition = Lists.newArrayList();
	}
	
	abstract public String getName();
	
	@PostConstruct
	protected void initializePageDef() {
		definePageMenu(this.pageMenuDefinition);
	}
	protected void definePageMenu(List<AppMenuItem> pageMenuDefinition) {}
	
	public Page newPageInstance(ApplicationContext applicationContext) {
		return applicationContext.getBean(this.pageType,this);
	}
	
	abstract public ActivityDef<?> getInitialActivity();
	
	public List<AppMenuItem> getPageMenu() {
		return pageMenuDefinition;
	}
}
