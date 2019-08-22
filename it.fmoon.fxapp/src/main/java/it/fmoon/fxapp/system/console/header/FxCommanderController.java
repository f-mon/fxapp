package it.fmoon.fxapp.system.console.header;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.google.common.collect.Sets.SetView;

import io.reactivex.subjects.BehaviorSubject;
import it.fmoon.fxapp.mvc.AbstractController;
import it.fmoon.fxapp.support.ViewUtils;
import it.fmoon.fxapp.system.console.fxcommands.CommandsQuery;
import it.fmoon.fxapp.system.console.fxcommands.FxCommand;
import it.fmoon.fxapp.system.console.fxcommands.FxCommandService;
import it.fmoon.fxapp.system.console.fxcommands.FxToggleCommand;
import it.fmoon.fxapp.system.console.fxcommands.SuggestionItem;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FxCommanderController extends AbstractController {

	@FXML TextField textField;
	
	private PopOver suggestionPopover;
	
	private BehaviorSubject<List<SuggestionItem>> currentSuggestions = BehaviorSubject.createDefault(Collections.emptyList());
	
	private ObjectProperty<SuggestionItem> focusedSuggestion = new SimpleObjectProperty<SuggestionItem>(null);
	private ObjectProperty<CommandsQuery> commandsQuery = new SimpleObjectProperty<CommandsQuery>(new CommandsQuery());
	
	private VBox suggestionsList;
	
	@Autowired
	private FxCommandService fxCommandService;
	
	
	@FXML
	public void initialize() {
		textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
			CommandsQuery commamdsQuery = this.commandsQuery.getValue();
	        this.commandsQuery.setValue(commamdsQuery.setText(newValue));
	    });
		textField.setOnKeyPressed((KeyEvent ke)->{
            KeyCode code = ke.getCode();
            if (KeyCode.UP.equals(code)) {
            	onKeyUp();
            	ke.consume();
            }
            else if (KeyCode.DOWN.equals(code)) {
            	onKeyDown();
            	ke.consume();
            }
            else if (KeyCode.ENTER.equals(code)) {
            	selectFocusedItem();
            	ke.consume();
            }
        });
		this.suggestionsList = new VBox(2);
		suggestionPopover = new PopOver(this.suggestionsList);
		suggestionPopover.setArrowLocation(ArrowLocation.TOP_CENTER);
		this.currentSuggestions.subscribe(this::showSuggestions);
		this.focusedSuggestion.addListener((observable,oldValue,newValue) -> {
			removeFocusFromView(getSuggestionItemView(oldValue));
			addFocusToView(getSuggestionItemView(newValue));
		});
		this.commandsQuery.addListener((observable,oldValue,newValue) -> {
			checkRecalcSuggestions(newValue);
		});
	}


	private void onKeyUp() {
		moveFocus(-1);
	}


	private void onKeyDown() {
		if (this.suggestionPopover.isShowing()) {
			moveFocus(1);
		} else {
			String text = textField.getText();
			CommandsQuery query = this.commandsQuery.get().setText(text);
			checkRecalcSuggestions(query);
		}
 	}


	private void selectFocusedItem() {
		SuggestionItem suggestionItem = this.focusedSuggestion.get();
		if (suggestionItem!=null) {
			FxCommand suggestedCommand = suggestionItem.getSuggestedCommand();
			CommandsQuery commandsQuery = this.commandsQuery.get();
			activateCommand(commandsQuery,suggestedCommand);
		}
	}


	private void activateCommand(CommandsQuery query, FxCommand activatedCommand) {
		if (activatedCommand instanceof FxToggleCommand) {
			this.commandsQuery.setValue(query.toggleFlagValue(activatedCommand.getName()));
		}
	}


	private void moveFocus(int move) {
		if (this.focusedSuggestion.get()!=null) {			
			int index = getSuggestionItemIndex(this.focusedSuggestion.get());
			int nextIndex = index+move;
			List<SuggestionItem> list = this.currentSuggestions.getValue();
			if (nextIndex>=0 && list.size()>nextIndex) {
				setFocusedSuggestion(list.get(nextIndex));
			}
		}
	}

	private void setFocusedSuggestion(SuggestionItem suggestionItem) {
		this.focusedSuggestion.set(suggestionItem);
	}

	private void checkRecalcSuggestions(CommandsQuery query) {
		List<SuggestionItem> suggestions = this.fxCommandService.getSuggestions(query);
		currentSuggestions.onNext(suggestions);   
	}
	
	private void showSuggestions(List<SuggestionItem> items) {

		suggestionsList.getChildren().clear();
		items.forEach(item -> {
			Node view = createSuggestionItemView(item);
			VBox.setVgrow(view, Priority.ALWAYS);
			suggestionsList.getChildren().add(view);
		});
		
		if (items.size()>0) {
			this.suggestionPopover.show(textField);
			this.setFocusedSuggestion(items.get(0));
		} else {
			this.suggestionPopover.hide();
			this.setFocusedSuggestion(null);
		}
	}
	
	protected Node createSuggestionItemView(SuggestionItem item) {		
		Node itemV = this.viewLoader.loader().loadView(this,"DefaultSuggestionItem");
		itemV.getProperties().put("SuggestionItem", item);
		((Label)itemV.lookup("#label")).setText(item.getSuggestedCommand().getName());
		return itemV;
	}
	
	protected int getSuggestionItemIndex(SuggestionItem item) {		
		for (ListIterator<Node> it = suggestionsList.getChildren().listIterator(); it.hasNext();) {
			if (Objects.equal(it.next().getProperties().get("SuggestionItem"),item)) {
				return it.previousIndex();
			}
		}
		return -1;
	}
	
	protected Node getSuggestionItemView(SuggestionItem item) {
		int index = getSuggestionItemIndex(item);
		ObservableList<Node> children = suggestionsList.getChildren();
		return (index>-1 && children.size()>index)?children.get(index):null;
	}

	private void addFocusToView(Node suggestionItemView) {
		if (suggestionItemView==null) {
			return;
		}
//		SuggestionItem suggestionItem = (SuggestionItem)suggestionItemView.getProperties().get("SuggestionItem");
//		System.out.println("addFocusToView: "+suggestionItem.getIndex());
		ViewUtils.setStyle(suggestionItemView, "-fx-background-color", "orange");
	}
	
	private void removeFocusFromView(Node suggestionItemView) {
		if (suggestionItemView==null) {
			return;
		}
//		SuggestionItem suggestionItem = (SuggestionItem)suggestionItemView.getProperties().get("SuggestionItem");
//		System.out.println("removeFocusFromView: "+suggestionItem.getIndex());
		ViewUtils.setStyle(suggestionItemView, "-fx-background-color", "yellow");
	}
}
