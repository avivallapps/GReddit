package dim.aviv.greddit.adaptersss;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dim.aviv.greddit.R;
import dim.aviv.greddit.models.Search;
import dim.aviv.greddit.util.Utility;

/**
 * Created by אביב on 23/03/2019.
 */

public class SearchesRecyclerAdapter extends RecyclerView.Adapter<SearchesRecyclerAdapter.ViewHolder> {
    private static final String TAG = "SearchesRecyclerAdapter";

    private ArrayList<Search> mSearches = new ArrayList<>();
    private OnSearchListener mOnSearchListener;

    public SearchesRecyclerAdapter(ArrayList<Search> mSearches, OnSearchListener mOnSearchListener) {
        this.mSearches = mSearches;
        this.mOnSearchListener = mOnSearchListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_list_item,parent,false);
         return  new ViewHolder(view,mOnSearchListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: checking date......");

        try {
            String month = mSearches.get(position).getTimestamp().substring(0, 2);
            month = Utility.getMonthFromNumber(month);
            String year = mSearches.get(position).getTimestamp().substring(3);
            String timestamp = month + " " + year;
            holder.timestamp.setText(timestamp);
            holder.title.setText(mSearches.get(position).getTitle());
            Log.d(TAG, "onBindViewHolder: checking date now......" + holder.timestamp);

        }catch (NullPointerException e){
      //      Log.e(TAG, "onBindViewHolder: "  ddd );
        }
    }

    @Override
    public int getItemCount() {
        return mSearches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title,timestamp;
        OnSearchListener onSearchListener;

        public ViewHolder(View itemView, OnSearchListener onSearchListener) {
            super(itemView);
            title = itemView.findViewById(R.id.search_title);
            timestamp = itemView.findViewById(R.id.search_timestamp);
            this.onSearchListener = onSearchListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onSearchListener.onSearchClick(getAdapterPosition());
        }
    }

    public interface  OnSearchListener{
        void onSearchClick(int position);
    }

}
