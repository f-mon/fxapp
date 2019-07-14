package it.fmoon.fxapp.controllers.login;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.AbstractActivity;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginActivity extends AbstractActivity {

	LoginActivityDef def;

	public LoginActivity(LoginActivityDef def) {
		super();
		this.def = def;
	}
	
}
