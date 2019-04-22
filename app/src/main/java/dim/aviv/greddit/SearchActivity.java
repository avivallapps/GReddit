package dim.aviv.greddit;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import dim.aviv.greddit.adaptersss.SearchesRecyclerAdapter;
import dim.aviv.greddit.models.Search;
import dim.aviv.greddit.persistence.SearchRepository;
import dim.aviv.greddit.util.VerticalSpacingItemDecorator;

/**
 * Created by אביב on 23/03/2019.
 */

public class SearchActivity extends AppCompatActivity implements
        SearchesRecyclerAdapter.OnSearchListener{

    private RecyclerView mRecyclerView;
    private Toolbar mToolBar;
    private ImageButton mBackArrow;


    private ArrayList<Search> mSearches = new ArrayList<>();
    private SearchesRecyclerAdapter mSearchesRecyclerAdapter;
    private SearchRepository mSearchRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mRecyclerView = findViewById(R.id.recyclerView);
        mToolBar = findViewById(R.id.notes_toolbar);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);



     //   findViewById(R.id.fab).setOnClickListener(this);

        mSearchRepository = new SearchRepository(this);

        initRecyclerView();
       retrieveNotes();
//           insertFakeNotes();
//

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       // setSupportActionBar((Toolbar)findViewById(R.id.notes_toolbar));
    }

    private void retrieveNotes(){
        mSearchRepository.retrieveSearchTask().observe(this, new Observer<List<Search>>() {
            @Override
            public void onChanged(@Nullable List<Search> searches) {

                if(mSearches.size() > 0){
                    mSearches.clear();
                }
                if(searches != null){
                    mSearches.addAll(searches);
                }
                mSearchesRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void insertFakeNotes(){
        for(int i=0; i< 1000; i++){
            Search search = new Search();
            search.setTitle("title # " + i);
           // note.setContent("content #: " + i);
            search.setTimestamp("Jan 2019");
            mSearches.add(search);
        }
        mSearchesRecyclerAdapter.notifyDataSetChanged();
        setTitle("Searches");
    }



    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mSearchesRecyclerAdapter = new SearchesRecyclerAdapter(mSearches,this);
        mRecyclerView.setAdapter(mSearchesRecyclerAdapter);
    }

    @Override
    public void onSearchClick(int position) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("selected_search",mSearches.get(position).getTitle());
        startActivity(intent);
    }

//    @Override
//    public void onClick(View view) {
//
//        Intent intent = new Intent(this,MainActivity.class);
//        startActivity(intent);
//    }

    private void deleteSearch(Search search){
        mSearches.remove(search);
        mSearchesRecyclerAdapter.notifyDataSetChanged();

        mSearchRepository.deleteSearch(search);
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


            deleteSearch(mSearches.get(viewHolder.getAdapterPosition()));
        }
    };

}
