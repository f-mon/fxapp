package it.fmoon.fxapp.controllers.appmenu;

import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jdi.BooleanValue;

import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.components.menu.PageMenuItem;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.mvc.Activity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;
import it.fmoon.fxapp.support.ViewUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

@Component("AppMenuItemController")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AppMenuItemController extends AbstractController {


	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private AppMenuItem appMenuItem;
	private boolean isPageMenu;
	private AppMenuController appMenuController;
	
	private BooleanProperty activated = new SimpleBooleanProperty(false);  

	@FXML FontIcon icon;
	@FXML Label label;
	@FXML HBox panel;
	
	public AppMenuItemController(AppMenuItem appMenuItem,AppMenuController appMenuController) {
		super();
		this.appMenuItem = appMenuItem;
		this.appMenuController = appMenuController;
	}
	
	@FXML
	public void initialize() {
		
		try {
			this.label.setText(appMenuItem.getLabel());
			this.icon.setIconLiteral(appMenuItem.getIcon());
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		
		if (appMenuItem instanceof PageMenuItem) {			
			PageMenuItem pageMenuItem = (PageMenuItem)appMenuItem;
			PageDef pageDef = pageMenuItem.getPageDef();
			panel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
				appMenuController.onPageMenuItemClick(pageMenuItem,pageDef,isPageMenu);
			});
		}
		if (appMenuItem instanceof ActivityMenuItem) {	
			ActivityMenuItem activityMenuItem = (ActivityMenuItem)appMenuItem;
			ActivityDef<?> activityDef = activityMenuItem.getActivityDef();
			panel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
				appMenuController.onActivityMenuItemClick(activityMenuItem,activityDef,isPageMenu);
			});
		}
		
		activated.addListener((observable,oldValue,newValue)->{
			if (newValue) {				
				ViewUtils.setStyle(panel,"-fx-background-color","yellow");
			} else {
				ViewUtils.setStyle(panel,"-fx-background-color","white");
			}
		});
	}

	public boolean isPageMenu() {
		return isPageMenu;
	}

	public void setPageMenu(boolean isPageMenu) {
		this.isPageMenu = isPageMenu;
	}

	public void checkMenuItemSelection(Activity currentActivity) {
		if (currentActivity!=null) {
			
			if (appMenuItem instanceof ActivityMenuItem) {
				ActivityDef<?> activityDef = ((ActivityMenuItem)appMenuItem).getActivityDef();
				boolean same = activityDef.getName().equals(currentActivity.getActivityDef().getName());
				activated.setValue(same);
				return;
			}
		}
		activated.setValue(false);
	}
	
}
