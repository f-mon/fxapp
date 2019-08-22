package it.fmoon.fxapp.system.console.fxcommands;

public class CommandsQuery {
	
	private final String text;

	public CommandsQuery(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public CommandsQuery setText(String text) {
		return new CommandsQuery(text);
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		CommandsQuery other = (CommandsQuery) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	
	
	
}
