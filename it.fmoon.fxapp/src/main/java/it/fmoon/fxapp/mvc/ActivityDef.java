package it.fmoon.fxapp.mvc;

import org.springframework.context.ApplicationContext;

public abstract class ActivityDef<A extends AbstractActivity> {

	abstract public String getName();
	
	abstract public A newActivityInstance(ApplicationContext applicationContext);
	
}
