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
import it.fmoon.fxapp.components.FxViewLoader;
import it.fmoon.fxapp.controllers.appmenu.AppMenuController;
import it.fmoon.fxapp.controllers.appnavbar.AppNavBarController;
import it.fmoon.fxapp.controllers.userinfo.UserInfoController;
import it.fmoon.fxapp.events.InitializeApplication;
import it.fmoon.fxapp.events.StartupApplication;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.mvc.Activity;
import it.fmoon.fxapp.support.ControllerStackViewContainerHelper;
import it.fmoon.fxapp.system.home.HomeActivityDef;
import it.fmoon.fxapp.system.homepage.HomePageDef;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

@Component
public class ApplicationController 
	extends AbstractController 
	implements ControllerStackViewContainer
{

	@Autowired
	private HomePageDef homePageDef;
	
	@Autowired
	private HomeActivityDef homeActivityDef;

	@Autowired
	ActivityManager activityManager;
	
	@Autowired
	ActivityAnimator activityAnimator;

	
	@FXML AnchorPane outerPanel;
	
	@FXML AnchorPane bodyPanel;
	@FXML StackPane bodyGroup;
	
	private PopOver userInfoPopover;
	
	
	@Autowired
	AppNavBarController appNavBarController;
	
	@Autowired
	AppMenuController appMenuController;
	
	@Autowired
	UserInfoController userInfoController;
		
	
	private ObjectProperty<AppMenuState> appMenuState;
	
	private ControllerStackViewContainer csvc;

	private Pane appMenuView;


	public ApplicationController(FxViewLoader viewLoader) {
		super(viewLoader);
		appMenuState = new SimpleObjectProperty<AppMenuState>(new AppMenuState(true,AppMenuOpenState.OPEN,AppMenuMode.MOUNT));
	}
	
	@FXML
	public void initialize() {
		this.csvc = new ControllerStackViewContainerHelper(()->bodyGroup, activityAnimator);
	}

	@EventListener
	public void onInitializeApplication(InitializeApplication event) {
		activityManager.registerApplicationRootPageAndActivity(homePageDef, homeActivityDef);
	}

	@EventListener
	public void onstartupApplication(StartupApplication event) {
		setupNavBar();
		setupAppMenu();
		updateAppMenuState();		
		activityManager.onCurrentActivity().subscribe(this::configureForActivity);
	}
	
	protected void setupNavBar() {
		Parent navBar = appNavBarController.getView();
		outerPanel.getChildren().add(navBar);
		AnchorPane.setTopAnchor(navBar,0.0);
		AnchorPane.setLeftAnchor(navBar,0.0);
		AnchorPane.setRightAnchor(navBar,0.0);
		appNavBarController.setMenuState(this.appMenuState);
		appNavBarController.onClose().subscribe(this::onClose);
		appNavBarController.onLogin().subscribe(this::onLogin);
		appNavBarController.onMenu().subscribe(this::onMenuToggle);
	}
	
	protected void  setupAppMenu() {
		this.appMenuView = (Pane)appMenuController.getView();
		outerPanel.getChildren().add(this.appMenuView);
		AnchorPane.setTopAnchor(this.appMenuView,42.0);
		AnchorPane.setLeftAnchor(this.appMenuView,0.0);
		AnchorPane.setBottomAnchor(this.appMenuView,0.0);
		appMenuController.setCurrentState(appMenuState.getValue());
		appMenuController.onToggleMenuMode().subscribe(this::onToggleMenuMode);
		DropShadow shadow1 = new DropShadow();
		shadow1.setOffsetX(5);
		shadow1.setOffsetY(0);
		this.appMenuView.setEffect(shadow1);
	}
	
	protected void configureForActivity(Activity currentActivity) {
		AppMenuState menuState = appMenuState.getValue();
		if (currentActivity.getActivityDef().isFullScreen()) {
			setAppMenuState(menuState.hide());
		} else {
			setAppMenuState(menuState.show());
		}
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
	
	public void onMenuToggle(ActionEvent event) {
		AppMenuState menuState = this.appMenuState.getValue();
		if (menuState.isClosed())
			setAppMenuState(menuState.open());
		else if (menuState.isOpen())
			setAppMenuState(menuState.close());
	}
	
	public void onToggleMenuMode(ActionEvent event) {
		AppMenuState menuState = this.appMenuState.getValue();
		AppMenuMode[] menuModes = AppMenuMode.values();
		setAppMenuState(new AppMenuState(
				menuState.getOpenState(),
				menuModes[(menuState.getMode().ordinal()+1)%menuModes.length])
		);
	}

	protected void setAppMenuState(AppMenuState menuState) {
		this.appMenuState.setValue(menuState);
		this.appMenuController.setCurrentState(menuState);
		updateAppMenuState();
	}

	protected void updateAppMenuState() {
		AppMenuState menuState = this.appMenuState.getValue();
		if (menuState.isHidden() || menuState.isClosed()) {
			this.appMenuView.setVisible(false);
			AnchorPane.setLeftAnchor(bodyPanel,0D);			
		}
		else {
			this.appMenuView.setVisible(true);
			double width = this.appMenuController.getWidth();
			this.appMenuView.setMinWidth(width);
			this.appMenuView.setMaxWidth(width);
			if (menuState.isOverlay()) {
				AnchorPane.setLeftAnchor(bodyPanel,0D);
			} else if (menuState.isMounted()) {
				AnchorPane.setLeftAnchor(bodyPanel,width);
			} else if (menuState.isBars()) {
				AnchorPane.setLeftAnchor(bodyPanel,width);
			}
		}
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
