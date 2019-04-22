package dim.aviv.greddit.async;

import android.os.AsyncTask;
import android.util.Log;

import dim.aviv.greddit.models.Search;
import dim.aviv.greddit.persistence.SearchDao;

/**
 * Created by אביב on 23/03/2019.
 */

public class InsertAsyncTask extends AsyncTask<Search,Void,Void> {
    private static final String TAG = "InsertAsyncTask";

    private SearchDao mSearchDao;
    public InsertAsyncTask(SearchDao dao){
        mSearchDao = dao;
    }

    @Override
    protected Void doInBackground(Search... searches) {
        Log.d(TAG, "doInBackground: thread: " + Thread.currentThread().getName());
        mSearchDao.insertSearches(searches);
        return null;
    }
}
