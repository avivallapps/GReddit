package dim.aviv.greddit;

import java.util.Map;

import dim.aviv.greddit.Comments.CheckComment;
import dim.aviv.greddit.account.CheckLogin;
import dim.aviv.greddit.model.Feed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by אביב on 09/03/2019.
 */

public interface RedditAPI {

  //  String BASE_URL = "https:/www.reddit.com/";
   // String BASE_URL = "https:/www.reddit.com/r/funny/";
    String BASE_URL = "https:/www.reddit.com/r/";


  @Headers("Content-Type: application/json")
  @GET("{feed_name}/.json")
    Call<Feed> getData(@Path("feed_name") String feed_name);
 //   @GET("users/{user}/reois")
   // Call<List<Repo>> listRepos(@Path("user") String user);


  @POST("{user}")
  Call<CheckLogin> signIn(
          @HeaderMap Map<String, String> headers,
          @Path("user") String username,
          @Query("user") String user,
          @Query("passwd") String password,
          @Query("api_type") String type
  );

  @POST("{comment}")
  Call<CheckComment> sumbitComment(
          @HeaderMap Map<String, String> headers,
          @Path("comment") String comment,
          @Query("parent") String parent,
          @Query("amp;text") String text
  );



//    @Headers("Content-Type: application/json")
//    @GET(".json")
//    Call<Feed> getData();
}
