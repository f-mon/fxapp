package it.fmoon.fxapp.system.console.fxcommands;

public class SuggestionItem {

	private int index;
	private FxCommand suggestedCommand;


	public void setIndex(int index) {
		this.index = index;
	}
	public int getIndex() {
		return index;
	}
	
	public FxCommand getSuggestedCommand() {
		return suggestedCommand;
	}
	public void setSuggestedCommand(FxCommand suggestedCommand) {
		this.suggestedCommand = suggestedCommand;
	}

}
