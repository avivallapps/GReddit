package dim.aviv.greddit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dim.aviv.greddit.account.LoginActivity;
import dim.aviv.greddit.model.Feed;
import dim.aviv.greddit.model.children.Children;
import dim.aviv.greddit.models.Search;
import dim.aviv.greddit.persistence.SearchRepository;
import dim.aviv.greddit.util.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

  //  private static final     String BASE_URL = "https:/www.reddit.com/r/funny/";
  //  private static final     String BASE_URL = "https:/www.reddit.com/r/";

    URLS urls = new URLS();

    private  Button btnRefreshFeed;
    private EditText mFeedName;
    private String currentFeed;

    private SearchRepository mSearchRepository;
    private Search mFinalSearch = new Search();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Log.d(TAG, "onCreate: starting.");
        btnRefreshFeed = (Button) findViewById(R.id.btnRefreshFeed);
        mFeedName = (EditText) findViewById(R.id.etFeedName);
        btnRefreshFeed = (Button) findViewById(R.id.btnRefreshFeed);

        mSearchRepository = new SearchRepository(this);

        setupToolbar();



     //   init();

        if(!getIncomingIntent()){

            mFeedName.setText(currentFeed);
            //this is a new note(edit mode)
          //  hideSoftKeyboard();
            init();
          //  enableEditMode();
        }
        else {
            //this is NOT a new note (view mode)

        }

        btnRefreshFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedName = mFeedName.getText().toString();
                Log.d(TAG, "feedname is: " + mFeedName.getText().toString());
                if(!feedName.equals("")){
                    Log.d(TAG, "onClick: yesfeedname:" + mFeedName.getText().toString() );
                    currentFeed = feedName;
                    init();
                }else {
                    Log.d(TAG, "onClick: nogeedname");
                    init();
                }
            }
        });
        //   Button btnGetData = (Button) findViewById(R.id.btnGetData);

    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null){
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_search")){
            //mInitialNote  = getIntent().getParcelableExtra("selected_note");
            String wassearched = getIntent().getStringExtra("selected_search");

            currentFeed = wassearched;
//            mFinalNote = new Note();
//            mFinalNote.setTitle(mInitialNote.getTitle());
//            mFinalNote.setContent(mInitialNote.getContent());
//            mFinalNote.setTimestamp(mInitialNote.getTimestamp());
//            mFinalNote.setId(mInitialNote.getId());

            // mFinalNote  = getIntent().getParcelableExtra("selected_note");
            Log.d(TAG, "getIncomingIntent: " + wassearched);
//
//            mMode = EDIT_MODE_DISABLED;
//            mIsNewNote = false;
            return false;
        }

        return true;

    }

    private void saveChanges(){
        saveNewSearch();
    }



    private void saveNewSearch(){
        mSearchRepository.insertSearchTask(mFinalSearch);
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
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        break;

                    case  R.id.navSearchHistory:
                        Intent intent1 = new Intent(MainActivity.this,SearchActivity.class);
                        startActivity(intent1);
                        break;
                }

                return false;
            }
        });
    }

    private void disabledEditMode(){
//        mBackArrowContainer.setVisibility(View.VISIBLE);
//        mCheckContainer.setVisibility(View.GONE);
//
//        mViewTitle.setVisibility(View.VISIBLE);
//        mEditTitle.setVisibility(View.GONE);
//
//        mMode = EDIT_MODE_DISABLED;

//        disableContentInteraction();


        String temp = mFeedName.getText().toString();
   //     temp = temp.replace("\n", "");
 //       temp = temp.replace(" ", "");
        if(temp.length() > 0){
            mFinalSearch.setTitle(mFeedName.getText().toString());
       //     mFinalSearch.setContent(mLinedEditText.getText().toString());
            String timestamp = Utility.getCurrentTimestamp();
            mFinalSearch.setTimestamp(timestamp);

            saveChanges();
//            if(!mFinalNote.getContent().equals(mInitialNote.getContent())
//                    || !mFinalNote.getTitle().equals(mInitialNote.getTitle())){
//                saveChanges();
//
//            }
        }
    }

    private String ConvertTheData(String thedate){

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
    }
    private void init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RedditAPI redditAPI = retrofit.create(RedditAPI.class);
        Call<Feed> call = redditAPI.getData(currentFeed);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {

                hideSoftKeyboard();
                if(getIntent().hasExtra("selected_search")) {
                    //mInitialNote  = getIntent().getParcelableExtra("selected_note");
                    String checknew = getIntent().getStringExtra("selected_search");
                    if(checknew.equals(currentFeed)){
                        Log.d(TAG, "onResponse: match! not saving search");
                    }else
                        disabledEditMode();
                }else{
                 //   disabledEditMode();

                }
                

                //   saveChanges();
               // Log.d(TAG, "onResponse: server Respone " + response.toString());
                //Log.d(TAG, "onResponse: recived information " + response.body().toString());

                final ArrayList<Post> posts = new ArrayList<Post>();


                try{



                        ArrayList<Children> childrenList = response.body().getData().getChildren();
                    Log.d(TAG, "onResponse: childrenlist " + childrenList.size());
                    if(childrenList.size() != 0){
                        disabledEditMode();
                    }else{
                        Toast.makeText(MainActivity.this,"There is not such " + currentFeed + " page.",Toast.LENGTH_SHORT).show();
                    }
                    for(int i=0; i<childrenList.size(); i++){
//                    Log.d(TAG, "onResponse: \n" +
//                    "kind: " + childrenList.get(i).getKind() + "\n" +
//                    "contest_mode: " + childrenList.get(i).getData().getContest_mode() + "\n" +
//                    "subreddit: " + childrenList.get(i).getData().getSubreddit() + "\n" +
//                    "author: " + childrenList.get(i).getData().getAuthor() + "\n" +
//                    "url: " + childrenList.get(i).getData().getUrl() + "\n" +
//                    "-------------------------------------------------------------------\n\n");

                        String finaldate = ConvertTheData(childrenList.get(i).getData().getCreated_utc());

                        try{



                            posts.add(new Post(
                                    childrenList.get(i).getData().getTitle(),
                                    childrenList.get(i).getData().getAuthor(),
                                    childrenList.get(i).getData().getThumbnail(),
                                    childrenList.get(i).getData().getPermalink(),
                                    finaldate,
                                    childrenList.get(i).getData().getName()
                            ));

                        }catch (NullPointerException e){

                            posts.add(new Post(
                                    childrenList.get(i).getData().getTitle(),
                                    "None",
                                    childrenList.get(i).getData().getThumbnail(),
                                    childrenList.get(i).getData().getPermalink(),
                                    finaldate,
                                    childrenList.get(i).getData().getName()
                            ));

                            Log.e(TAG, "onResponse: NullPointerException" + e.getMessage() );
                        }


                    }

//                for(int j=0;j<posts.size(); j++){
//                    Log.d(TAG, "onResponse: \n" +
//                            "Title:: " + posts.get(j).getTitle() + "\n" +
//                            "Author: " + posts.get(j).getAuthor() + "\n" +
//                            "Url: " + posts.get(j).getThumbnailURL() + "\n" +
//                            "PostUrl: " + posts.get(j).getPostURL() + "\n" +
//                            "Date: " + posts.get(j).getDate_updated() + "\n");
//
//                }

                    ListView listView = (ListView) findViewById(R.id.listView);
                    CustomListAdapter customListAdapter = new CustomListAdapter(MainActivity.this,R.layout.card_layout_main,posts);
                    listView.setAdapter(customListAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d(TAG, "onItemClick: clicked" + posts.get(i).toString());
                            Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
                            intent.putExtra(getString(R.string.post_url),posts.get(i).getPostURL());
                            intent.putExtra(getString(R.string.post_thumbnail),posts.get(i).getThumbnailURL());
                            intent.putExtra(getString(R.string.post_title),posts.get(i).getTitle());
                            intent.putExtra(getString(R.string.post_author),posts.get(i).getAuthor());
                            intent.putExtra(getString(R.string.date_updated),posts.get(i).getDate_updated());
                            intent.putExtra(getString(R.string.post_name),posts.get(i).getName());
                            startActivity(intent);
                        }
                    });
//                    if(!getIntent().hasExtra("selected_search")) {
//                        disabledEditMode();
//                    }

                }catch (NullPointerException e){
                    Log.e(TAG, "onResponse: not such a page." + e.getMessage() );
                    Toast.makeText(MainActivity.this,"There is not such " + currentFeed + " page.",Toast.LENGTH_SHORT).show();
                }

            }



            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

                Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage() );
            //    Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;

    }
}
