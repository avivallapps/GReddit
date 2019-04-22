package dim.aviv.greddit.persistence;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


import dim.aviv.greddit.models.Search;

/**
 * Created by אביב on 23/03/2019.
 */


@Dao
public interface SearchDao {
    @Insert
    long[] insertSearches(Search...searches);

    @Query("SELECT * FROM searches")
    LiveData<List<Search>> getSearches();

    @Query("SELECT * FROM searches WHERE title Like :title")
    List<Search> getSearchWithCustomQuery(String title);

    @Delete
    int delete(Search... searches);

    @Update
    int update(Search... searches);

}


