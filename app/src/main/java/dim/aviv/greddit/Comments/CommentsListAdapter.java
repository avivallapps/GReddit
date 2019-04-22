package dim.aviv.greddit.Comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import dim.aviv.greddit.R;

/**
 * Created by אביב on 11/03/2019.
 */

public class CommentsListAdapter extends ArrayAdapter<Comment> {

        private static final String TAG = "CustomListAdapter";

        private Context mContext;
        private int mResource;
        private int lastPosition = -1;

        /**
         * Holds variables in a View
         */
        private static class ViewHolder {
             TextView comment;
             TextView author;
             TextView date_updated;
             ProgressBar mProgressbar;
        }

        /**
         * Default constructor for the PersonListAdapter
         * @param context
         * @param resource
         * @param objects
         */
        public CommentsListAdapter(Context context, int resource, ArrayList<Comment> objects) {
            super(context, resource, objects);
            mContext = context;
            mResource = resource;

            //sets up the image loader library
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            //get the persons information
            String title = getItem(position).getComment();
            String author = getItem(position).getAuthor();
            String date_created = getItem(position).getUpdated();

            try{


                //create the view result for showing the animation
                final View result;

                //ViewHolder object
                final ViewHolder holder;

                if(convertView == null){
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    convertView = inflater.inflate(mResource, parent, false);
                    holder= new ViewHolder();
                    holder.comment = (TextView) convertView.findViewById(R.id.comment);
                    holder.author = (TextView) convertView.findViewById(R.id.commentAuthor);
                    holder.date_updated = (TextView) convertView.findViewById(R.id.commentUpdated);
                    holder.mProgressbar = (ProgressBar) convertView.findViewById(R.id.commentProgressBar);

                    result = convertView;

                    convertView.setTag(holder);
                }
                else{
                    holder = (ViewHolder) convertView.getTag();
                    result = convertView;
                    holder.mProgressbar.setVisibility(View.VISIBLE);

                }


                lastPosition = position;

                holder.comment.setText(title);
                holder.author.setText(author);
                holder.date_updated.setText(date_created);
                holder.mProgressbar.setVisibility(View.GONE);




                return convertView;
            }catch (IllegalArgumentException e){
                Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
                return convertView;
            }

        }

    }