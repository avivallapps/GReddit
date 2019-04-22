package dim.aviv.greddit.persistence;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;

import dim.aviv.greddit.async.DeleteAsyncTask;
import dim.aviv.greddit.async.InsertAsyncTask;
import dim.aviv.greddit.async.UpdateAsyncTask;
import dim.aviv.greddit.models.Search;

/**
 * Created by אביב on 23/03/2019.
 */

public class SearchRepository {

    private SearchDatabase mSearchDatabase;

    public SearchRepository(Context context) {
        mSearchDatabase = SearchDatabase.getInstnace(context);
    }

    public void insertSearchTask(Search search){
        new InsertAsyncTask(mSearchDatabase.getSearchDao()).execute(search);

    }

    public void updateSearch(Search search){
        new UpdateAsyncTask(mSearchDatabase.getSearchDao()).execute(search);
    }
    public LiveData<List<Search>> retrieveSearchTask(){
        return mSearchDatabase.getSearchDao().getSearches();
    }
    public void deleteSearch(Search search){

        new DeleteAsyncTask(mSearchDatabase.getSearchDao()).execute(search);
    }
}