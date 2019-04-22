package dim.aviv.greddit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by אביב on 27/01/2019.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }


    //Arrays
    public int [] slide_images = {

//            R.drawable.tipon,
//            R.drawable.toaddnewphoto,
//            R.drawable.choosingphoto,
//            R.drawable.sharethephoto,
//            R.drawable.postplusprofileicon,
//            R.drawable.profilepicturee

            R.drawable.redditmain,
            R.drawable.redditmain2,
            R.drawable.redditsearch3,
            R.drawable.redditsearch35,
            R.drawable.redditsearch4,
            R.drawable.redditgotoweb,
            R.drawable.reddittheweb,
            R.drawable.redditsearch45,
            R.drawable.redditreply5,
            R.drawable.redditbefbeflogin,
            R.drawable.menuchoose,
            R.drawable.searchhistoryy,
            R.drawable.redditloginscreeen
    };

    public String[] slide_heading = {

            "The main Page",
            "Reedit search",
            "Reddit search results",
            "Reddit search results",
            "View Post's comments.",
            "View post on Webpage",
            "View post on Webpage",
            "Post your own comment",
            "Post new comment",
            "Navigate to LoginPage/SearchHistory",
            "Search History",
            "Login Page"

    };
    public String[] slide_descp = {

            "On this page you will start your search in Reddit. ",
            "Enter here what you want to search in Reddit.",
            "Here you will see all the posts are relevance with your search.",
            "Click one of the posts to see the comments.",
            "Here you will see all the comments on this post.",
            "By Clicking on the post image you will transfer to the post Web.",
            "On the post Web you can see all the post details including watch the post video and comments.",
            "Back to the comments screen, Click one of the comments to reply to a comment or click the reply button to add your own comment to the post.",
            "Enter your comment and press Post comment" + "\n Be aware that you will need to login to your account to post a comment. if you will try to post a comment when you are not logged in you will get a message.",
            "Click to Login to Your Account or Watch your SearchHistory",
            "SearchHistory Page. you can click on your search history to search subject again and manage your history by delete subjects.",
            "Login Page. please Login to your Account to be able to comment on posts."
    };
    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        SquareImageView slideImageView = (SquareImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_descp);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_heading[position]);
        slideDescription.setText(slide_descp[position]);

        container.addView(view);
        return view;

    }

    //stop at the last page.
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
