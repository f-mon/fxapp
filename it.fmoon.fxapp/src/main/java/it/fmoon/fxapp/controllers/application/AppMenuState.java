package it.fmoon.fxapp.controllers.application;

public class AppMenuState {
	
	private final boolean hidden;
	private final AppMenuOpenState openState;
	private final AppMenuMode mode;
	
	public AppMenuState(AppMenuOpenState openState, AppMenuMode mode) {
		this(false,openState,mode);
	}
	public AppMenuState(boolean hidden,AppMenuOpenState openState, AppMenuMode mode) {
		this.hidden = hidden;
		this.openState = openState;
		this.mode = mode;
	}

	public AppMenuOpenState getOpenState() {
		return openState;
	}

	public AppMenuMode getMode() {
		return mode;
	}

	public boolean isHidden() {
		return this.hidden;
	}
	
	public boolean isClosed() {
		return AppMenuOpenState.CLOSED.equals(this.openState);
	}
	public boolean isOpen() {
		return AppMenuOpenState.OPEN.equals(this.openState);
	}
	
	public boolean isOverlay() {
		return AppMenuMode.OVERLAY.equals(this.mode);
	}
	public boolean isMounted() {
		return AppMenuMode.MOUNT.equals(this.mode);
	}
	public boolean isBars() {
		return AppMenuMode.BAR_WITH_ICONS.equals(this.mode);
	}

	public AppMenuState open() {
		return new AppMenuState(AppMenuOpenState.OPEN, this.mode);
	}
	public AppMenuState close() {
		 return new AppMenuState(AppMenuOpenState.CLOSED, this.mode);
	}
	public AppMenuState hide() {
		 return new AppMenuState(true, this.openState, this.mode);
	}
	public AppMenuState show() {
		 return new AppMenuState(false, this.openState, this.mode);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (hidden ? 1231 : 1237);
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result + ((openState == null) ? 0 : openState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppMenuState other = (AppMenuState) obj;
		if (hidden != other.hidden)
			return false;
		if (mode != other.mode)
			return false;
		if (openState != other.openState)
			return false;
		return true;
	}
	
}
