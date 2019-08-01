package it.fmoon.fxapp.mvc;

public class ActivityContext<A extends Activity> {

	private final A activity;

	public ActivityContext(A activity) {
		super();
		this.activity = activity;
	}

	public A getActivity() {
		return activity;
	}
	
}