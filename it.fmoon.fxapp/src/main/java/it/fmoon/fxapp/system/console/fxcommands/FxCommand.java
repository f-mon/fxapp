package it.fmoon.fxapp.system.console.fxcommands;

public interface FxCommand {
	
	default String getName() {
		return this.getClass().getSimpleName();
	}
	
}
