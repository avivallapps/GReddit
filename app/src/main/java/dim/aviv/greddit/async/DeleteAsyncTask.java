package dim.aviv.greddit.async;

import android.os.AsyncTask;

import dim.aviv.greddit.models.Search;
import dim.aviv.greddit.persistence.SearchDao;

/**
 * Created by אביב on 23/03/2019.
 */

public class DeleteAsyncTask extends AsyncTask<Search,Void,Void>{
    private SearchDao mSearchDao;

    public DeleteAsyncTask(SearchDao mSearchDao) {
        this.mSearchDao = mSearchDao;
    }

    @Override
    protected Void doInBackground(Search... searches) {
        mSearchDao.delete(searches);
        return null;
    }
}
