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
import it.fmoon.fxapp.support.HttpJsonClient;
import it.fmoon.fxapp.system.home.HomeActivityDef;
import it.fmoon.fxapp.system.home.HomePageDef;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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
	
	@Autowired
	HttpJsonClient httpJsonClient;
		
	
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
		
		httpJsonClient.sendAndReadValue(
				"https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US",
				"images",0,"url")
			.subscribe(url->{
				
				BackgroundImage bingBackground= new BackgroundImage(new Image("https://www.bing.com"+url),
						BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
						BackgroundSize.DEFAULT);
				outerPanel.setBackground(new Background(bingBackground));
			});
		
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
		Node navBar = appNavBarController.getView();
		DropShadow shadow1 = new DropShadow(10,Color.BLACK);
		navBar.setEffect(shadow1);
		outerPanel.getChildren().add(navBar);
		AnchorPane.setTopAnchor(navBar,0.0);
		AnchorPane.setLeftAnchor(navBar,0.0);
		AnchorPane.setRightAnchor(navBar,0.0);
		appNavBarController.setMenuState(this.appMenuState);
		appNavBarController.onClose().subscribe(this::onClose);
		appNavBarController.onLogin().subscribe(this::onLogin);
		appNavBarController.onMenu().subscribe(this::onMenuToggle);
		appNavBarController.getNavBarHeight().addListener((obj,oldVval,newVal)->{
			AnchorPane.setTopAnchor(this.appMenuView,getNavBarHeight());
			AnchorPane.setTopAnchor(this.bodyPanel,getNavBarHeight());
		});
		AnchorPane.setTopAnchor(this.bodyPanel,getNavBarHeight());
	}
	
	protected void  setupAppMenu() {
		this.appMenuView = (Pane)appMenuController.getView();
		outerPanel.getChildren().add(this.appMenuView);
		AnchorPane.setTopAnchor(this.appMenuView,getNavBarHeight());
		AnchorPane.setLeftAnchor(this.appMenuView,0.0);
		AnchorPane.setBottomAnchor(this.appMenuView,0.0);
		appMenuController.setCurrentState(appMenuState.getValue());
		appMenuController.onToggleMenuMode().subscribe(this::onToggleMenuMode);
		DropShadow shadow1 = new DropShadow(10,Color.BLACK);
//		shadow1.setOffsetX(5);
//		shadow1.setOffsetY(0);
		this.appMenuView.setEffect(shadow1);
	}
	
	private double getNavBarHeight() {
		return this.appNavBarController.getNavBarHeight().getValue().doubleValue();
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
			animateAppMenuToWidth(0D);			
		}
		else {
			this.appMenuView.setVisible(true);
			double width = this.appMenuController.getWidth();
			if (menuState.isOverlay()) {
				animateAppMenuToWidth(width,0D);
			} else {				
				animateAppMenuToWidth(width);
			}
		}
	}

	private void animateAppMenuToWidth(double appMenuToWidth) {
		animateAppMenuToWidth(appMenuToWidth, appMenuToWidth);
	}
	
	private void animateAppMenuToWidth(double appMenuToWidth,double bodyPanelToLeft) {
		double initialValue = appMenuView.getWidth();
		double delta = appMenuToWidth - initialValue;
		
		double initialValueLeft = AnchorPane.getLeftAnchor(bodyPanel);
		double deltaLeft = bodyPanelToLeft - initialValueLeft;
		
		final Animation animation = new Transition() {
			
		     {
		         setCycleDuration(Duration.millis(200));
		     }

		     protected void interpolate(double frac) {
		    	 
		         final double width = initialValue + ( delta * frac);
		         final double left = initialValueLeft + ( deltaLeft * frac);
		         
		         appMenuView.setMinWidth(width);
		         appMenuView.setPrefWidth(width);
		         appMenuView.setMaxWidth(width);
		         AnchorPane.setLeftAnchor(bodyPanel,left);
		     }
		     
		     

		 };
		 
		 animation.play();
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
