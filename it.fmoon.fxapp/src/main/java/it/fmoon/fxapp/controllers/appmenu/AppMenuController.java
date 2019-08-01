package it.fmoon.fxapp.controllers.appmenu;

import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.components.FxLoader;
import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.components.menu.MenuManager;
import it.fmoon.fxapp.components.menu.PageMenuItem;
import it.fmoon.fxapp.controllers.application.AppMenuState;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

@Component
public class AppMenuController extends AbstractController {

	private static final double WIDTH = 300D;
	private static final double WIDTH_IN_BARMODE = 60D;
	
	public enum AppMenuViewState {
		APPLICATION_MENU, PAGE_MENU
	}

	private PublishSubject<ActionEvent> onToggleMenuMode = PublishSubject.create();
	
	private AppMenuState currentState;
	private AppMenuViewState appMenuViewState = AppMenuViewState.PAGE_MENU;

	@FXML
	StackPane bodyGroup;

	private VBox applicationMenuPanel;
	private VBox pageMenuPanel;

	@Autowired
	private MenuManager menuManager;

	@Autowired
	private ActivityManager activityManager;

	@Autowired
	private FxLoader loader;

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
	
	@FXML
	public void onChangeMenuMode(ActionEvent event) {
		this.onToggleMenuMode.onNext(event);
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
				Node view = createActivityMenuItemView((ActivityMenuItem) menuItem,isPageMenu);
				menuPanel.getChildren().add(view);
			} else if (menuItem instanceof PageMenuItem) {
				Node view = createPageMenuItemView((PageMenuItem) menuItem,isPageMenu);
				menuPanel.getChildren().add(view);
			}
		});
	}

	protected Node createPageMenuItemView(PageMenuItem menuItem, boolean isPageMenu) {
		PageDef pageDef = menuItem.getPageDef();
		Node menuItemV = loader.loadView(this,"AppMenuItem");
		((Label)menuItemV.lookup("#label")).setText(menuItem.getLabel());
		((FontIcon)menuItemV.lookup("#icon")).setIconLiteral(menuItem.getIcon());
		menuItemV.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			onPageMenuItemClick(menuItem,pageDef,isPageMenu);
		});
		return menuItemV;
	}

	protected Node createActivityMenuItemView(ActivityMenuItem menuItem, boolean isPageMenu) {
		ActivityDef<?> activityDef = menuItem.getActivityDef();
		Node menuItemV = loader.loadView(this,"AppMenuItem");
		((Label)menuItemV.lookup("#label")).setText(menuItem.getLabel());
		((FontIcon)menuItemV.lookup("#icon")).setIconLiteral(menuItem.getIcon());
		menuItemV.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
			onActivityMenuItemClick(menuItem,activityDef,isPageMenu);
		});
		return menuItemV;
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

	public Observable<ActionEvent> onToggleMenuMode() {
		return this.onToggleMenuMode;
	}

	public AppMenuState getCurrentState() {
		return currentState;
	}
	public void setCurrentState(AppMenuState menuState) {
		this.currentState = menuState;
		if (this.currentState.isBars()) {
			((BorderPane)this.getView()).setMaxWidth(WIDTH_IN_BARMODE);
		} else {
			((BorderPane)this.getView()).setMaxWidth(WIDTH);
		}
	}

	public double getWidth() {
		if (this.currentState.isBars()) {
			return WIDTH_IN_BARMODE;
		}
		return WIDTH;
	}
}
