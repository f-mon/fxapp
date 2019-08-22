package it.fmoon.fxapp.system.console;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.prototype.search.AbstractSearchActivity;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FxConsoleActivity extends AbstractSearchActivity<FxConsoleActivityDef> {

	public FxConsoleActivity(FxConsoleActivityDef def) {
		super(def);
	}

}
