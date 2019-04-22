package dim.aviv.greddit.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import dim.aviv.greddit.models.Search;

/**
 * Created by אביב on 23/03/2019.
 */


@Database(entities = {Search.class}, version =  1,exportSchema = false)
public abstract class SearchDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "search_db";

    private static SearchDatabase instance;

    static SearchDatabase getInstnace(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    SearchDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract  SearchDao getSearchDao();
}


