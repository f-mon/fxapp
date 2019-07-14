package it.fmoon.fxapp.controllers.login;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.ActivityDef;

@Component
public class LoginActivityDef extends ActivityDef<LoginActivity> {

	@Override
	public String getName() {
		return "loginActivity";
	}
	
	public LoginActivity newActivityInstance(ApplicationContext applicationContext) {
		return applicationContext.getBean(LoginActivity.class,this);
	}

	
}
