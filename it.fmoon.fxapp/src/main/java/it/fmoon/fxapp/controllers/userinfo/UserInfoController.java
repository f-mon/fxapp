package it.fmoon.fxapp.controllers.userinfo;

import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.components.ActivityManager;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.system.login.LoginActivityDef;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

@Component
public class UserInfoController 
	extends AbstractController
{

	@Autowired
	LoginActivityDef loginActivityDef;

	@Autowired
	ActivityManager activityManager;

	private PopOver popoverContainer;
	
	@FXML public void onLogout(ActionEvent event) {}

	@FXML public void onLogin(ActionEvent event) {
		this.popoverContainer.hide();
		activityManager.startActivity(loginActivityDef).subscribe();
	}

	public void setPopover(PopOver popoverContainer) {
		this.popoverContainer = popoverContainer;
	}

}
