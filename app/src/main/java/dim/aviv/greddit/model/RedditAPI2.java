package dim.aviv.greddit.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by אביב on 11/03/2019.
 */

public interface RedditAPI2 {

    //  String BASE_URL = "https:/www.reddit.com/";
    // String BASE_URL = "https:/www.reddit.com/r/funny/";
    String BASE_URL = "https:/www.reddit.com/r/";


    @Headers("Content-Type: application/json")
    @GET("{feed_name}/.json")
    Call<List<Feed>> getData(@Path("feed_name") String feed_name);
    //   @GET("users/{user}/reois")
    // Call<List<Repo>> listRepos(@Path("user") String user);





//    @Headers("Content-Type: application/json")
//    @GET(".json")
//    Call<Feed> getData();
}
