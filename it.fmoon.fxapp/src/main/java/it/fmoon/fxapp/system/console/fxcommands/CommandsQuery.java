package it.fmoon.fxapp.system.console.fxcommands;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class CommandsQuery {
	
	private final String text;
	private final ImmutableMap<String,Object> aspects;

	public CommandsQuery() {
		this("",null);
	}
	public CommandsQuery(String text,Map<String,Object> properties) {
		super();
		this.text = text;
		this.aspects = properties!=null?ImmutableMap.copyOf(properties):ImmutableMap.of();
	}

	public String getText() {
		return text;
	}

	public CommandsQuery setText(String text) {
		return new CommandsQuery(text,this.aspects);
	}
	
	public CommandsQuery toggleFlagAspect(String name) {
		Map<String,Object> mutableProperties = mutableAspects();
		mutableProperties.compute(name, (k,v)->(v==null)?Boolean.TRUE:!((Boolean)v));
		return new CommandsQuery(text,mutableProperties);
	}
	
	public ImmutableMap<String,Object> getAspects() {
		return this.aspects;
	}

	
	
	private Map<String, Object> mutableAspects() {
		return new HashMap<String, Object>(this.aspects);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aspects == null) ? 0 : aspects.hashCode());
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
		if (aspects == null) {
			if (other.aspects != null)
				return false;
		} else if (!aspects.equals(other.aspects))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	
}
