package it.fmoon.fxapp.components.menu;

public abstract class AppMenuItem {
	
	protected final String label;
	protected final String icon;

	public AppMenuItem(String label, String icon) {
		super();
		this.label = label;
		this.icon = icon;
	}
	
	public String getIcon() {
		return icon;
	}
	public String getLabel() {
		return label;
	}
	
	
	protected static abstract class AppMenuItemBuilder<B extends AppMenuItemBuilder<B,M>,M extends AppMenuItem> {

		protected String label;
		protected String icon;
		
		
		public B label(String label) {
			this.label = label;
			return self();
		}
		

		public B icon(String icon) {
			this.icon = icon;
			return self();
		}
		
		@SuppressWarnings("unchecked")
		protected B self() {
			return (B)this;
		}
		
		abstract public M build();
		
	}

}
