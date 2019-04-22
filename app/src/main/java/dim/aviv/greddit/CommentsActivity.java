package dim.aviv.greddit;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dim.aviv.greddit.Comments.CheckComment;
import dim.aviv.greddit.Comments.Comment;
import dim.aviv.greddit.Comments.CommentsListAdapter;
import dim.aviv.greddit.account.LoginActivity;
import dim.aviv.greddit.model.Feed;
import dim.aviv.greddit.model.RedditAPI2;
import dim.aviv.greddit.model.children.Children;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by אביב on 11/03/2019.
 */

public class CommentsActivity extends AppCompatActivity {

    private static final String TAG = "CommentsActivity";

   // private static final  String BASE_URL = "https:/www.reddit.com/r/";

    URLS urls = new URLS();

    private static String postURL;
    private static String postThumbnail;
    private static String postTitle;
    private static String postAuthor;
    private static String postUpdated;
    private static String PostName;


    private String modhash;
    private String cookie;
    private String username;

    private int defaultImage;

    private String currentFeed;
    private ListView mListView;

    private ArrayList<Comment> mComments;

    private ProgressBar mProgressBar;
    private TextView progressText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Log.d(TAG, "onCreate: stating..");


        mProgressBar = (ProgressBar) findViewById(R.id.commentsLoaingbar);
        progressText = (TextView) findViewById(R.id.progressText);

        setupToolbar();

        getSessionParms();

        mProgressBar.setVisibility(View.VISIBLE);


        setupImageLoader();

        initPost();

        init();
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.d(TAG, "onMenuItemClick: clicked menu item: " + menuItem);

                switch (menuItem.getItemId()){
                    case R.id.navLogin:
                        Intent intent = new Intent(CommentsActivity.this,LoginActivity.class);
                        startActivity(intent);

                    case  R.id.navSearchHistory:
                        Intent intent1 = new Intent(CommentsActivity.this,SearchActivity.class);
                        startActivity(intent1);
                }
                return false;
            }
        });
    }

    private void init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedditAPI2 redditAPI = retrofit.create(RedditAPI2.class);

        Log.d(TAG, "ohohohoh " + currentFeed);
        Call<List<Feed>> call = redditAPI.getData(currentFeed);

        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {

                Log.d(TAG, "onResponse: blablablablabla");

                Log.d(TAG, "onResponse: server Respone " + response.toString());
                Log.d(TAG, "onResponse: recived information " + response.body().toString());


                mComments = new ArrayList<Comment>();

                for(Feed feed: response.body()) {
                        Log.d(TAG, "getit!: " + feed.toString());

                    ArrayList<Children> childrenList = feed.getData().getChildren();
                    for (int i = 0; i < childrenList.size(); i++) {
//                        Log.d(TAG, "onResponse: \n" +
//                                "kind: " + childrenList.get(i).getKind() + "\n" +
//                                "contest_mode: " + childrenList.get(i).getData().getContest_mode() + "\n" +
//                                "subreddit: " + childrenList.get(i).getData().getSubreddit() + "\n" +
//                                "author: " + childrenList.get(i).getData().getAuthor() + "\n" +
//                                //     "url: " + childrenList.get(i).getData().getUrl() + "\n" +
//                                "-------------------------------------------------------------------\n\n");

                        Log.d(TAG, "this is the time of date:" + childrenList.get(i).getData().getCreated_utc() );
                        String finaldate = ConvertTheData(childrenList.get(i).getData().getCreated_utc());

                        try{
                            mComments.add(new Comment(
                                    childrenList.get(i).getData().getBody(),
                                    childrenList.get(i).getData().getAuthor(),
                                    finaldate,
                                    childrenList.get(i).getData().getName()
                                    //  finaldate

                            ));
                        }catch (IndexOutOfBoundsException e){
                            mComments.add(new Comment(
                                    "Error reaing comment",
                                    "None",
                                    "None",
                                    "None"
                                    //  finaldate
                            ));
                            Log.e(TAG, "onResponse: IndexOutOfBoundsException" + e.getMessage());
                        }
                        catch (NullPointerException e){
                            mComments.add(new Comment(
                                    childrenList.get(i).getData().getBody(),
                                    "None",
                                    finaldate,
                                    childrenList.get(i).getData().getName()
                                    //  finaldate

                            ));
                            Log.e(TAG, "onResponse: NullPointerException" + e.getMessage());
                        }
                    }
                    mListView = (ListView) findViewById(R.id.commentsListView);
                    CommentsListAdapter adapter = new CommentsListAdapter(CommentsActivity.this,R.layout.comments_layout,mComments);
                    mListView.setAdapter(adapter);

                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            getUserComment(mComments.get(i).getId());
                        }
                    });

                    mProgressBar.setVisibility(View.GONE);
                    progressText.setText("");
                }


               // List <String> commentsDetails =

                     //   mComments.add(new Comment())
               // Log.d(TAG, "onResponse:  " + response.);

             //   Gson gson = new Gson();
            //    TypeToken<List<Children>> token = new TypeToken<List<Children>>(){};
              //  List<Children> personList = gson.fromJson(response, token.getType());





//                Gson gson = new Gson();
                //List<List<Feed>>  firstList = gson.fromJson(response.body(),new TypeToken<List<List<Feed>>>(){}.getType());
            //    List<Feed> feedList = response.

//                List<Feed> feedArrayList = response.body();
//
//                 ArrayList<Children> childrenArrayList = new ArrayList<Children>();
//
//                for(int i=0; i<feedArrayList.size(); i++){
//
//                    Log.d(TAG, "how many times?");
//
//                        childrenArrayList = feedArrayList.get(i).getData().getChildren();
//
//                    for(int j=0; j<childrenArrayList.size();j++){
//
//                        Log.d(TAG, "onResponse: \n" +
//                    "kind: " + childrenArrayList.get(i).getKind() + "\n" +
//                    "contest_mode: " + childrenArrayList.get(i).getData().getContest_mode() + "\n" +
//                    "subreddit: " + childrenArrayList.get(i).getData().getSubreddit() + "\n" +
//                    "author: " + childrenArrayList.get(i).getData().getAuthor() + "\n" +
//                 //   "url: " + childrenArrayList.get(i).getData().getUrl() + "\n" +
//                    "-------------------------------------------------------------------\n\n");
//                    }
//
////                    Log.d(TAG, "onResponse: \n" +
////                    "kind: " + feedArrayList.get(i).getKind() + "\n" +
////                    "Feed: " + feedArrayList.get(i).getData().getChildren() +
////                    "-------------------------------------------------------------------\n");
//
//                }


             //   ArrayList<Children> childrenList = response.body().getData().getChildren();
           //     for(int i=0; i<childrenList.size(); i++){
//                    Log.d(TAG, "onResponse: \n" +
//                    "kind: " + childrenList.get(i).getKind() + "\n" +
//                    "contest_mode: " + childrenList.get(i).getData().getContest_mode() + "\n" +
//                    "subreddit: " + childrenList.get(i).getData().getSubreddit() + "\n" +
//                    "author: " + childrenList.get(i).getData().getAuthor() + "\n" +
//                    "url: " + childrenList.get(i).getData().getUrl() + "\n" +
//                    "-------------------------------------------------------------------\n\n");



                    //    List<Children> childernss = response.body()
               // List<Children> childrens = response.body().getData().getChildren();

//                for( int i=0; i< childrens.size();i++) {
//                    Log.d(TAG, "onResponse: childrenn: " + childrens.get(i).toString() +
//                            "\n----------------------------------------------- \n");
//
//
//                }
//                }
            }

            @Override
            public void onFailure(Call<List<Feed>> call, Throwable t) {

                Log.e(TAG, "onResponse: not such a page." + t.getMessage() );
//                Toast.makeText(CommentsActivity.this,"There is not such " + currentFeed + " page.",Toast.LENGTH_SHORT).show();
            }
        });

//        call.enqueue(new Callback<Feed>() {
//            @Override
//            public void onResponse(Call<Feed> call, Response<Feed> response) {
//
/////
//   ///             Log.d(TAG, "onResponse: blablablablabla");
//
// //               Log.d(TAG, "onResponse: server Respone " + response.toString());
/////                Log.d(TAG, "onResponse: recived information " + response.body().toString());
//
////                List<Children> childrens = response.body().getData().getChildren();
////
////                for( int i=0; i< childrens.size();i++){
////                    Log.d(TAG, "onResponse: childrenn: "  + childrens.get(i).toString() +
////                            "\n----------------------------------------------- \n");
////
////
////                }
//            }
//
//            @Override
//            public void onFailure(Call<Feed> call, Throwable t) {
//
//                Log.e(TAG, "onResponse: not such a page." + t.getMessage() );
//                Toast.makeText(CommentsActivity.this,"There is not such " + currentFeed + " page.",Toast.LENGTH_SHORT).show();
//            }
//        });
    }




    private void initPost(){

     //   Log.d(TAG, "banana");


        Intent incomingIntent = getIntent();
        postURL = incomingIntent.getStringExtra(getString(R.string.post_url));
        postThumbnail = incomingIntent.getStringExtra(getString(R.string.post_thumbnail));
        postTitle = incomingIntent.getStringExtra(getString(R.string.post_title));
        postAuthor = incomingIntent.getStringExtra(getString(R.string.post_author));
        postUpdated = incomingIntent.getStringExtra(getString(R.string.date_updated));
        PostName = incomingIntent.getStringExtra(getString(R.string.post_name));


        TextView title = (TextView) findViewById(R.id.postTitle);
        TextView author = (TextView) findViewById(R.id.postAuthor);
        TextView updated = (TextView) findViewById(R.id.postUpdated);
        ImageView thumbnail = (ImageView) findViewById(R.id.postThumbnail);
        Button btnReply = (Button) findViewById(R.id.btnPostReply);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.postsLoaingbar);



        Log.d(TAG, "this is the imageurl:" + postThumbnail);

        title.setText(postTitle);
        author.setText(postAuthor);
        updated.setText(postUpdated);
        displayImage(postThumbnail,thumbnail,progressBar);

        
        try{

            String [] splitURL = postURL.split("/r/");
            Log.d(TAG, "this is the thing: " + postURL);
            currentFeed = splitURL[1];
            currentFeed = removeLastChar(currentFeed);
        //    Log.d(TAG, "this is the thing: " + splitURL[1]);
            Log.d(TAG, "initPost: current feed: " + currentFeed);

        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "initPost: ArrayIndexOutOfBoundsException" + e.getMessage());
        }

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: reply.");
                getUserComment(PostName);
            }
        });

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Opening URL in webciew" + postUpdated);
                Intent intent = new Intent(CommentsActivity.this,WebViewActivity.class);
                intent.putExtra("url", postURL);
                startActivity(intent);
            }
        });

       
    }

    private void getUserComment(final String post_name){
        final Dialog dialog = new Dialog(CommentsActivity.this);
        dialog.setTitle("dialog");
        dialog.setContentView(R.layout.comment_input_dialog);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.6);

        dialog.getWindow().setLayout(width,height);
        dialog.show();

        Button btnPostComment = (Button) dialog.findViewById(R.id.btnPostComment);
        final EditText comment = (EditText) dialog.findViewById(R.id.dialogComment);

        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: going to post comment.");
                ///post comment stuff for retrofit

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(urls.COMMENT_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RedditAPI redditAPI = retrofit.create(RedditAPI.class);

                HashMap<String, String> headermap = new HashMap<>();
                headermap.put("User-Agent", username);
                headermap.put("X-Modhash", modhash);
                headermap.put("cookie", "reddit_session=" + cookie);

                Log.d(TAG, ": btnPostComment: \n " +
                        "username: " + username + "\n" +
                        "modhash: " + modhash + "\n" +
                        "cookie: " + cookie + "\n" );


                String theComment = comment.getText().toString();
                Call<CheckComment> call = redditAPI.sumbitComment(headermap,"comment",post_name,theComment);

                call.enqueue(new Callback<CheckComment>() {
                    @Override
                    public void onResponse(Call<CheckComment> call, Response<CheckComment> response) {
                  //      Log.d(TAG, "onResponse: server Respone " + response.body().toString());

                        try{
                          //  Log.d(TAG, "onResponse: server Respone " + response.body().toString());
                            Log.d(TAG, "onResponse: server Respone " + response.toString());

                            String postSuccess = response.body().getSuccess();

                            if (postSuccess.equals("true")){
                                dialog.dismiss();
                                Toast.makeText(CommentsActivity.this,"Post Successful",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(CommentsActivity.this,"You didn't signed in. please Login to your account.",Toast.LENGTH_SHORT).show();

                            }

                        }catch (NullPointerException e){
                            Log.e(TAG, "onResponse: NullPointerException" + e.getMessage() );
                        }


                    }

                    @Override
                    public void onFailure(Call<CheckComment> call, Throwable t) {

                        Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
                        Toast.makeText(CommentsActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    private void displayImage(String imageURL, ImageView imageView, final ProgressBar progressBar) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = CommentsActivity.this.getResources().getIdentifier("@drawable/thedefaultt",null,CommentsActivity.this.getPackageName());

        //create display options
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        Log.d(TAG, "displayImage: trying to:" + imageURL.toString());
        //download and display image from url
        imageLoader.displayImage(imageURL, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);


            }
        });
    }

    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                CommentsActivity.this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

         defaultImage = CommentsActivity.this.getResources().getIdentifier("@drawable/thedefaultt",null,CommentsActivity.this.getPackageName());

    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private String ConvertTheData(String thedate){


        try{

            Double d = Double.parseDouble(thedate);
            long unixSeconds = d.longValue();
            // long unixSeconds = Long.valueOf(thedate);

// convert seconds to milliseconds
            Date date = new Date(unixSeconds*1000L);
// the format of your date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
// give a timezone reference for formatting (see comment at the bottom)
            // sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
            String formattedDate = sdf.format(date);
            return formattedDate;

        }catch (NullPointerException e){
            Log.e(TAG, "ConvertTheData: NullPointerException" + e.getMessage());
            return null;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;

    }
    private  void getSessionParms(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CommentsActivity.this);

        username = preferences.getString("@string/SessionUsername", "");
        modhash = preferences.getString("@string/SessionModhash", "");
        cookie = preferences.getString("@string/SessionCookie", "");


        Log.d(TAG, "setSessionParams: storing session variables: \n " +
                "username: " + username + "\n" +
                "modhash: " + modhash + "\n" +
                "cookie: " + cookie + "\n" );
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume: resuming activity");
        getSessionParms();
    }
}
