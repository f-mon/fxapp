package it.fmoon.fxapp.system.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.mvc.AbstractActivity;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ElementsActivity extends AbstractActivity<ElementsActivityDef> {

	public ElementsActivity(ElementsActivityDef def) {
		super(def);
	}

}
