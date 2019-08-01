package it.fmoon.fxapp.prototype.search;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import it.fmoon.fxapp.mvc.ActivityContext;
import it.fmoon.fxapp.mvc.ActivityDef;

public class SearchActivityDef<A extends AbstractSearchActivity<?>> extends ActivityDef<A> {

	public SearchActivityDef(Class<A> activityType) {
		super(activityType);
	}

	public SearchDataProvider<?> getDataProvider() {
		return (ctx)->Single.just(Arrays.asList(1,2,3,4));
	}

	public static class SearchContext<A extends AbstractSearchActivity<?>> extends ActivityContext<A> {

		public SearchContext(A activity) {
			super(activity);
		}
		
	}
	
	@FunctionalInterface
	public interface SearchDataProvider<I> {
		
		Single<List<I>> getData(SearchContext<?> ctx);
		
	}
	
}
