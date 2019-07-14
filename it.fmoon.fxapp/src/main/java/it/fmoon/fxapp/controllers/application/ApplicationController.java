package it.fmoon.fxapp.controllers.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.reactivex.Single;
import it.fmoon.fxapp.components.ActivityAnimator;
import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.controllers.home.HomeActivityDef;
import it.fmoon.fxapp.controllers.login.LoginActivityDef;
import it.fmoon.fxapp.events.InitializeApplication;
import it.fmoon.fxapp.events.StartupApplication;
import it.fmoon.fxapp.mvc.AbstractController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;

@Component
public class ApplicationController extends AbstractController {

	@Autowired
	HomeActivityDef home;

	@Autowired
	LoginActivityDef login;

	@Autowired
	ActivityManager activityManager;
	
	@Autowired
	ActivityAnimator activityAnimator;

	
	@FXML
	StackPane bodyGroup;

	@FXML BorderPane sideMenuPanel;

	@EventListener
	public void onInitializeApplication(InitializeApplication event) {

	}

	@EventListener
	public void onstartupApplication(StartupApplication event) {
		activityManager.startActivity(home)
			.subscribe();
	}
	
	@FXML
	public void onClose(ActionEvent event) {
		activityManager.stopActivity().subscribe();
	}

	@FXML
	public void onLogin(ActionEvent event) {
		activityManager.startActivity(login).subscribe();
	}

	
	public Single<Boolean> pauseControllerView(AbstractController ac) {
		Parent view = ac.getView();
		if (this.bodyGroup.getChildren().contains(view)) {
			return activityAnimator.removeAnimate((Node) view, this.bodyGroup,true)
					.flatMap(n->Single.just(Boolean.TRUE));
		}
		return Single.just(Boolean.TRUE);
	}
	
	public Single<Boolean> removeStoppedControllerView(AbstractController ac) {
		Parent view = ac.getView();
		if (this.bodyGroup.getChildren().contains(view)) {
			return activityAnimator.removeAnimate((Node) view, this.bodyGroup,false)
				.flatMap((n)->Single.just(Boolean.TRUE));
		}
		return Single.just(Boolean.TRUE);
	}


	public Single<Boolean> showStartedControllerView(AbstractController ac) {
		Parent view = ac.getView();
		if (!this.bodyGroup.getChildren().contains(view)) {
			return activityAnimator.insertAnimate((Node) view, this.bodyGroup,false)
				.flatMap((n)->Single.just(Boolean.TRUE));
		}
		return Single.just(Boolean.TRUE);
	}

	public Single<Boolean> showResumedControllerView(AbstractController ac) {
		Parent view = ac.getView();
		if (!this.bodyGroup.getChildren().contains(view)) {
			return activityAnimator.insertAnimate((Node) view, this.bodyGroup,true)
				.flatMap((n)->Single.just(Boolean.TRUE));
		}
		return Single.just(Boolean.TRUE);
	}

	@FXML public void onMenu(ActionEvent event) {
		this.sideMenuPanel.setVisible(true);
	}

	@FXML public void onMenuClose(ActionEvent event) {
		this.sideMenuPanel.setVisible(false);
	}
}
