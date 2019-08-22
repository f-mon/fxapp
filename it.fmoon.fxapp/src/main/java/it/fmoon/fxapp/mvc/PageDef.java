package it.fmoon.fxapp.mvc;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;

import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.components.menu.AppMenus;
import it.fmoon.fxapp.support.NamesUtils;

public abstract class PageDef {

	protected final Class<? extends Page> pageType;
	protected final List<AppMenuItem> pageMenuDefinition;
	
	protected Class<? extends AbstractController> pageHeaderController;
	
	public PageDef() {
		this(BasePageImpl.class);
	}
	public PageDef(Class<? extends Page> pageType) {
		this.pageType = pageType;
		this.pageMenuDefinition = Lists.newArrayList();
	}
	
	public String getName() {
		return NamesUtils.pageNameFromPageDefName(this.getClass().getSimpleName());
	}
	
	@PostConstruct
	protected void initializePageDef() {
		definePageMenu(this.pageMenuDefinition);
	}
	protected void definePageMenu(List<AppMenuItem> pageMenuDefinition) {}
	
	public Page newPageInstance(ApplicationContext applicationContext) {
		return applicationContext.getBean(this.pageType,this);
	}
	
	public ActivityDef<?> getInitialActivity() {
		return AppMenus.findFirstActivity(pageMenuDefinition)
			.map(ActivityMenuItem::getActivityDef)
			.get();
	}
	
	public List<AppMenuItem> getPageMenu() {
		return pageMenuDefinition;
	}
	
	public String getLabel() {
		return getName();
	}
	public String getIcon() {
		return "fa-file-o";
	}
	
	
	public Class<? extends AbstractController> getPageHeaderController() {
		return pageHeaderController;
	}
	protected void setPageHeaderController(Class<? extends AbstractController> pageHeaderController) {
		this.pageHeaderController = pageHeaderController;
	}
	
	
}
