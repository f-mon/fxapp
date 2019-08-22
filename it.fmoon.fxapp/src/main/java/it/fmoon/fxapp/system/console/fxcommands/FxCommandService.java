package it.fmoon.fxapp.system.console.fxcommands;

import java.util.List;

public interface FxCommandService {

	List<SuggestionItem> getSuggestions(CommandsQuery query);

}
