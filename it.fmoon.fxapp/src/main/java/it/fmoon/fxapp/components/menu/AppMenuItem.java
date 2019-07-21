package it.fmoon.fxapp.components.menu;

public abstract class AppMenuItem {
	
	protected String label;
	protected String icon;

	public AppMenuItem icon(String icon) {
		this.icon = icon;
		return this;
	}
	public String getIcon() {
		return icon;
	}
	
	public AppMenuItem label(String label) {
		this.label = label;
		return this;
	}
	public String getLabel() {
		return label;
	}

}
