package it.fmoon.fxapp.controllers.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.reactivex.Single;
import it.fmoon.fxapp.components.ActivityAnimator;
import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.events.InitializeApplication;
import it.fmoon.fxapp.events.StartupApplication;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.support.ControllerStackViewContainerHelper;
import it.fmoon.fxapp.system.homepage.HomePageDef;
import it.fmoon.fxapp.system.login.LoginActivityDef;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

@Component
public class ApplicationController 
	extends AbstractController 
	implements ControllerStackViewContainer
{

	@Autowired
	private HomePageDef homePageDef;

	@Autowired
	LoginActivityDef login;

	@Autowired
	ActivityManager activityManager;
	
	@Autowired
	ActivityAnimator activityAnimator;

	@FXML
	StackPane bodyGroup;

	@FXML BorderPane sideMenuPanel;
	
	private ControllerStackViewContainer csvc;


	@EventListener
	public void onInitializeApplication(InitializeApplication event) {

	}

	@EventListener
	public void onstartupApplication(StartupApplication event) {
		activityManager.startPage(homePageDef)
			.subscribe();
	}
	
	@FXML
	public void initialize() {
		this.csvc = new ControllerStackViewContainerHelper(()->bodyGroup, activityAnimator);
	}
	
	@FXML
	public void onClose(ActionEvent event) {
		activityManager.stopActivity().subscribe();
	}

	@FXML
	public void onLogin(ActionEvent event) {
		activityManager.startActivity(login).subscribe();
	}

	@FXML public void onMenu(ActionEvent event) {
		this.sideMenuPanel.setVisible(true);
	}

	@FXML public void onMenuClose(ActionEvent event) {
		this.sideMenuPanel.setVisible(false);
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
