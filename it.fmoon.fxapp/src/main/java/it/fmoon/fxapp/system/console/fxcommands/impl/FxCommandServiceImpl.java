package it.fmoon.fxapp.system.console.fxcommands.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.fmoon.fxapp.system.console.fxcommands.CommandsQuery;
import it.fmoon.fxapp.system.console.fxcommands.FxCommand;
import it.fmoon.fxapp.system.console.fxcommands.FxCommandService;
import it.fmoon.fxapp.system.console.fxcommands.SuggestionItem;

@Component
public class FxCommandServiceImpl implements FxCommandService {

	@Autowired
	private List<FxCommand> commands;
	
	@Override
	public List<SuggestionItem> getSuggestions(CommandsQuery query) {
		
		return commands.stream().filter(c->{
				return c.getName().toUpperCase().startsWith(query.getText().toUpperCase());
			})
			.map(c->{
				SuggestionItem suggestionItem = new SuggestionItem();
				suggestionItem.setSuggestedCommand(c);
				return suggestionItem;
			})
			.collect(Collectors.toList());
		
	}


}
