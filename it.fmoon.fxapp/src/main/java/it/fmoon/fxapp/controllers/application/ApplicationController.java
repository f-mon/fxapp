package it.fmoon.fxapp.controllers.application;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.reactivex.Single;
import it.fmoon.fxapp.components.ActivityAnimator;
import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.components.ControllerStackViewContainer;
import it.fmoon.fxapp.controllers.appmenu.AppMenuController;
import it.fmoon.fxapp.controllers.appnavbar.AppNavBarController;
import it.fmoon.fxapp.controllers.userinfo.UserInfoController;
import it.fmoon.fxapp.events.InitializeApplication;
import it.fmoon.fxapp.events.StartupApplication;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.support.ControllerStackViewContainerHelper;
import it.fmoon.fxapp.system.homepage.HomePageDef;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

@Component
public class ApplicationController 
	extends AbstractController 
	implements ControllerStackViewContainer
{

	private static final double SIDE_MENU_WIDTH = 300D;

	@Autowired
	private HomePageDef homePageDef;

	@Autowired
	ActivityManager activityManager;
	
	@Autowired
	ActivityAnimator activityAnimator;

	
	@FXML AnchorPane outerPanel;
	
	@FXML AnchorPane bodyPanel;
	@FXML StackPane bodyGroup;

	@FXML BorderPane sideMenuPanel;
	
	private PopOver userInfoPopover;
	
	
	@Autowired
	AppNavBarController appNavBarController;
	
	@Autowired
	AppMenuController appMenuController;
	
	@Autowired
	UserInfoController userInfoController;
		
	private ObjectProperty<AppMenuState> appMenuState = new SimpleObjectProperty<AppMenuState>(AppMenuState.OPEN_MOUNTED);
	
	private ControllerStackViewContainer csvc;



	@EventListener
	public void onInitializeApplication(InitializeApplication event) {

	}

	@EventListener
	public void onstartupApplication(StartupApplication event) {
		activityManager.startPage(homePageDef)
			.subscribe();
		
		Parent navBar = appNavBarController.getView();
		outerPanel.getChildren().add(navBar);
		AnchorPane.setTopAnchor(navBar,0.0);
		AnchorPane.setLeftAnchor(navBar,0.0);
		AnchorPane.setRightAnchor(navBar,0.0);
		
		sideMenuPanel.setCenter(appMenuController.getView());
		
		updateAppMenuState();
		appNavBarController.setMenuState(this.appMenuState);
		appNavBarController.onClose().subscribe(this::onClose);
		appNavBarController.onLogin().subscribe(this::onLogin);
		appNavBarController.onMenu().subscribe(this::onMenu);
		
	}
	
	@FXML
	public void initialize() {
		this.csvc = new ControllerStackViewContainerHelper(()->bodyGroup, activityAnimator);
		DropShadow shadow1 = new DropShadow();
		shadow1.setOffsetX(5);
		shadow1.setOffsetY(0);
		sideMenuPanel.setEffect(shadow1);
	}
	
	public void onClose(ActionEvent event) {
		activityManager.stopActivity().subscribe();
	}
	public void onLogin(ActionEvent event) {
		if (userInfoPopover==null) {			
			userInfoPopover = new PopOver(this.userInfoController.getView());
			this.userInfoController.setPopover(userInfoPopover);
			userInfoPopover.setArrowLocation(ArrowLocation.TOP_RIGHT);
		}
		userInfoPopover.show((Node)event.getSource());
	}
	
	public void onMenu(ActionEvent event) {
		switch (this.appMenuState.getValue()) {
		case CLOSED:
			setAppMenuState(AppMenuState.OPEN_OVERLAY);
			break;

		case OPEN_OVERLAY:
			setAppMenuState(AppMenuState.OPEN_MOUNTED);
			break;
			
		case OPEN_MOUNTED:
			setAppMenuState(AppMenuState.CLOSED);
			break;
		};
	}

	protected void setAppMenuState(AppMenuState menuState) {
		this.appMenuState.setValue(menuState);
		updateAppMenuState();
	}

	protected void updateAppMenuState() {
		switch (this.appMenuState.getValue()) {
		case CLOSED:
			this.sideMenuPanel.setVisible(false);
			AnchorPane.setLeftAnchor(bodyPanel,0D);
			break;

		case OPEN_OVERLAY:
			this.sideMenuPanel.setVisible(true);
			AnchorPane.setLeftAnchor(bodyPanel,0D);
			break;
			
		case OPEN_MOUNTED:
			this.sideMenuPanel.setVisible(true);
			AnchorPane.setLeftAnchor(bodyPanel,SIDE_MENU_WIDTH);
			break;
		};
	}

	public Single<Boolean> pauseControllerView(AbstractController ac) {
		return csvc.pauseControllerView(ac);
	}

	public Single<Boolean> removeStoppedControllerView(AbstractController ac) {
		return csvc.removeStoppedControllerView(ac);
	}

	public Single<Boolean> showStartedControllerView(AbstractController ac) {
		return csvc.showStartedControllerView(ac);
	}

	public Single<Boolean> showResumedControllerView(AbstractController ac) {
		return csvc.showResumedControllerView(ac);
	}

}
