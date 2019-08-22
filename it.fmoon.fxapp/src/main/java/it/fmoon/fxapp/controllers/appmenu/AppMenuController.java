package it.fmoon.fxapp.controllers.appmenu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.components.menu.ActivityMenuItem;
import it.fmoon.fxapp.components.menu.AppMenuItem;
import it.fmoon.fxapp.components.menu.MenuManager;
import it.fmoon.fxapp.components.menu.PageMenuItem;
import it.fmoon.fxapp.controllers.application.AppMenuState;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.mvc.Activity;
import it.fmoon.fxapp.mvc.ActivityDef;
import it.fmoon.fxapp.mvc.PageDef;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@FXML
	StackPane bodyGroup;

	private VBox applicationMenuPanel;
	private VBox pageMenuPanel;

	@Autowired
	private MenuManager menuManager;

	@Autowired
	private ActivityManager activityManager;

//	@Autowired
//	private FxLoader loader;
	
	@Autowired
	private BeanFactory beanFactory;

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
		
		this.activityManager.onCurrentActivity().subscribe(currAct->{
			checkMenuItemSelection(currAct);
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
			Node view = createMenuItemView( menuItem,isPageMenu);
			menuPanel.getChildren().add(view);
		});
	}

	protected Node createMenuItemView(AppMenuItem menuItem, boolean isPageMenu) {
		AppMenuItemController menuItemController = beanFactory.getBean(AppMenuItemController.class, menuItem, this);
		menuItemController.setPageMenu(isPageMenu);
		return menuItemController.getView();
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
		BorderPane borderPane = (BorderPane)this.getView();
		if (this.currentState.isBars()) {
			borderPane.setMinWidth(WIDTH_IN_BARMODE);
			borderPane.setMaxWidth(WIDTH_IN_BARMODE);
		} else {
			borderPane.setMinWidth(WIDTH);
			borderPane.setMaxWidth(WIDTH);
		}
	}

	public double getWidth() {
		if (this.currentState.isBars()) {
			return WIDTH_IN_BARMODE;
		}
		return WIDTH;
	}
	
	
	private void checkMenuItemSelection(Activity currAct) {
		this.pageMenuPanel.getChildrenUnmodifiable().forEach(node->{
			Object controller = node.getProperties().get("controller");
			((AppMenuItemController)controller).checkMenuItemSelection(currAct);
		});
	}
}
