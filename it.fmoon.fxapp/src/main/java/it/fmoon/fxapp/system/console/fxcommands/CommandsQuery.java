package it.fmoon.fxapp.system.console.fxcommands;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class CommandsQuery {
	
	private final String text;
	private final ImmutableMap<String,Object> properties;

	public CommandsQuery() {
		this("",null);
	}
	public CommandsQuery(String text,Map<String,Object> properties) {
		super();
		this.text = text;
		this.properties = properties!=null?ImmutableMap.copyOf(properties):ImmutableMap.of();
	}

	public String getText() {
		return text;
	}

	public CommandsQuery setText(String text) {
		return new CommandsQuery(text,this.properties);
	}
	
	public CommandsQuery toggleFlagValue(String name) {
		Map<String,Object> mutableProperties = mutableProperties();
		mutableProperties.compute(name, (k,v)->(v==null)?Boolean.TRUE:!((Boolean)v));
		return new CommandsQuery(text,mutableProperties);
	}

	
	
	private Map<String, Object> mutableProperties() {
		return new HashMap<String, Object>(this.properties);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
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
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	
}
