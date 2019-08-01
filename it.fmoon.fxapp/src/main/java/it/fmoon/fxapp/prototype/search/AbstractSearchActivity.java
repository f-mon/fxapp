package it.fmoon.fxapp.prototype.search;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import it.fmoon.fxapp.mvc.AbstractActivity;
import it.fmoon.fxapp.prototype.search.SearchActivityDef.SearchContext;
import it.fmoon.fxapp.prototype.search.SearchActivityDef.SearchDataProvider;
import it.fmoon.fxapp.support.FxViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class AbstractSearchActivity<D extends SearchActivityDef<?>> 
	extends AbstractActivity<D>
	implements FxViewFactory
{

	protected SearchDataProvider<?> searchDataProvider;
	
	private BehaviorSubject<List<?>> searchResults = BehaviorSubject.createDefault(Collections.emptyList());
	
	@FXML VBox listView;

	public AbstractSearchActivity(D def) {
		super(def);
	}
	
	@PostConstruct
	public void initializeActivity() {
		super.initializeActivity();
		initSearchDataProvider();
		this.searchResults.subscribe(this::applySearchResult);
	}

	protected void initSearchDataProvider() {
		this.searchDataProvider = this.getActivityDef().getDataProvider();
	}

	@Override
	public Node createView() {
		return this.viewLoader.loader().loadLinkedView(AbstractSearchActivity.class,"SearchActivityView",this);
	}

	@FXML public void onSearch(ActionEvent event) {
		doSearch().subscribe();
	}
	
	public Single<Boolean> doSearch() {
		SearchContext<AbstractSearchActivity<?>> searchContext = new SearchContext<AbstractSearchActivity<?>>(this);
		return searchDataProvider.getData(searchContext)
				.map(this::publishSearchResult);
	}
	
	protected Boolean publishSearchResult(List<?> items) {
		this.searchResults.onNext(items);
		return Boolean.TRUE;
	}
	
	protected void applySearchResult(List<?> items) {
		if (listView==null) {
			return;
		}
		listView.getChildren().clear();
		items.forEach(item -> {
			Node view = createItemView(item);
			VBox.setVgrow(view, Priority.ALWAYS);
			listView.getChildren().add(view);
		});
	}

	protected Node createItemView(Object item) {		
		Node itemV = this.viewLoader.loader().loadView(AbstractSearchActivity.class,"DefaultItem");
		return itemV;
	}
	
}
