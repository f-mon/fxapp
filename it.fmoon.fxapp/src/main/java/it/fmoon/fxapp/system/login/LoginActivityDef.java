package it.fmoon.fxapp.system.login;

import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.ActivityDef;

@Component
public class LoginActivityDef extends ActivityDef<LoginActivity> {

	public LoginActivityDef() {
		super(LoginActivity.class);
	}

	@Override
	public boolean isFullScreen() {
		return true;
	}
	
}
