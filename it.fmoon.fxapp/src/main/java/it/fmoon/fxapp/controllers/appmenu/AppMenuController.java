package it.fmoon.fxapp.controllers.appmenu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.components.menu.MenuManager;
import it.fmoon.fxapp.components.menu.PageMenuItem;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

@Component
public class AppMenuController extends AbstractController {

	public enum AppMenuViewState {
		APPLICATION_MENU, PAGE_MENU
	}

	private AppMenuViewState appMenuViewState = AppMenuViewState.PAGE_MENU;

	@FXML
	StackPane bodyGroup;

	private VBox applicationMenuPanel;
	private VBox pageMenuPanel;

	@Autowired
	private MenuManager menuManager;

	@Autowired
	private ActivityManager activityManager;

	@FXML
	public void initialize() {

		this.applicationMenuPanel = new VBox();
		this.pageMenuPanel = new VBox();
		
		this.menuManager.getApplicationMenusObs().subscribe(appMenuList->{
			this.redrawMenu(this.applicationMenuPanel,appMenuList,false);
		});
		this.menuManager.getCurrentPageMenusObs().subscribe(appMenuList->{
			this.redrawMenu(this.pageMenuPanel,appMenuList,true);
		});
		updateStateView();
	}

	@FXML
	public void onHomeMenu(ActionEvent event) {
		this.appMenuViewState = (this.appMenuViewState == AppMenuViewState.APPLICATION_MENU)
				? AppMenuViewState.PAGE_MENU
				: AppMenuViewState.APPLICATION_MENU;
		
		updateStateView();
		
	}
	
	private void updateStateView() {
		if (this.appMenuViewState==AppMenuViewState.APPLICATION_MENU) {
			this.bodyGroup.getChildren().remove(this.pageMenuPanel);
			this.bodyGroup.getChildren().add(this.applicationMenuPanel);
		}
		
		if (this.appMenuViewState==AppMenuViewState.PAGE_MENU) {
			this.bodyGroup.getChildren().remove(this.applicationMenuPanel);
			this.bodyGroup.getChildren().add(this.pageMenuPanel);
		}
		
	}

	protected void redrawMenu(VBox menuPanel, List<AppMenuItem> appMenuList, boolean isPageMenu) {
		menuPanel.getChildren().clear();
		appMenuList.forEach(menuItem -> {
			if (menuItem instanceof ActivityMenuItem) {
				Region view = createActivityMenuItemView((ActivityMenuItem) menuItem,isPageMenu);
				menuPanel.getChildren().add(view);
			} else if (menuItem instanceof PageMenuItem) {
				Region view = createPageMenuItemView((PageMenuItem) menuItem,isPageMenu);
				menuPanel.getChildren().add(view);
			}
		});
	}

	protected Region createPageMenuItemView(PageMenuItem menuItem, boolean isPageMenu) {
		PageDef pageDef = menuItem.getPageDef();
		Button button = new Button("Page: " + menuItem.getLabel());
		button.setOnAction((ActionEvent e) -> {
			onPageMenuItemClick(menuItem,pageDef,isPageMenu);
		});
		return button;
	}

	protected Region createActivityMenuItemView(ActivityMenuItem menuItem, boolean isPageMenu) {
		ActivityDef<?> activityDef = menuItem.getActivityDef();
		Button button = new Button("Activity: " + menuItem.getLabel());
		button.setOnAction((ActionEvent e) -> {
			onActivityMenuItemClick(menuItem,activityDef,isPageMenu);
		});
		return button;
	}

	protected void onActivityMenuItemClick(ActivityMenuItem menuItem, ActivityDef<?> activityDef, boolean isPageMenu) {
		if (isPageMenu) {			
			activityManager.startRootPageActivity(activityDef).subscribe();
		} else {
			activityManager.startActivity(activityDef).subscribe();
		}
	}

	protected void onPageMenuItemClick(PageMenuItem menuItem, PageDef pageDef, boolean isPageMenu) {
		if (isPageMenu) {			
			activityManager.startPage(pageDef).subscribe();
		} else {
			activityManager.startRootPage(pageDef).subscribe();
		}
	}
}
