package dim.aviv.greddit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoardActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;


    private SliderAdapter sliderAdapter;

    private Button mNextBtn;
    private Button mBackBtn;
    private Button mFinishBtn;

    private int mCurrentPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_on_board);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE)
                .getBoolean("isFirstRun",false);

        if (isFirstRun){
            startActivity(new Intent(OnBoardActivity.this,MainActivity.class));
            finish();
        }

        getSharedPreferences("PREFERENCE",MODE_PRIVATE).edit()
                .putBoolean("isFirstRun",true).apply();

           mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
           mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);


           mNextBtn = (Button) findViewById(R.id.nextBtn);
           mBackBtn = (Button) findViewById(R.id.prevBtn);
           mFinishBtn = (Button) findViewById(R.id.finishBtn);


           sliderAdapter = new SliderAdapter(this);
           mSlideViewPager.setAdapter(sliderAdapter);


           addDotsIndicator(0);

           mSlideViewPager.addOnPageChangeListener(viewListener);

           mNextBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   mSlideViewPager.setCurrentItem(mCurrentPage +1);
               }
           });

           mBackBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   mSlideViewPager.setCurrentItem(mCurrentPage -1);
               }
           });

           mFinishBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Intent intent = new Intent(OnBoardActivity.this, MainActivity.class);
                   startActivity(intent);
                   finish();

               }
           });
    }

    public void addDotsIndicator(int position){

        mDots = new TextView[13];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(30);
            mDots[i].setTextColor(getResources().getColor(R.color.colorGrey));

            mDotLayout.addView(mDots[i]);

        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            mCurrentPage = i;

            if(i == 0){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mFinishBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);
                mFinishBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("Next");
                mBackBtn.setText("");
                mFinishBtn.setText("");

            } else if (i == mDots.length -1 ){

                mNextBtn.setEnabled(false);
                mBackBtn.setEnabled(true);
                mFinishBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setVisibility(View.INVISIBLE);
                mFinishBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("");
                mBackBtn.setText("Back");
                mFinishBtn.setText("Finish");
            } else {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mFinishBtn.setEnabled(false);

                mBackBtn.setVisibility(View.VISIBLE);
                mFinishBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("Next");
                mBackBtn.setText("Back");
                mFinishBtn.setText("");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}

//        addFragment(new Step.Builder().setTitle("The main page.")
//                .setContent("This is the main Page that you will see every time you will log in. \n here you also will your own posts " +
//                        " and the posts from users you are following on.")
//                .setBackgroundColor(Color.parseColor("#cccccc")) // int background color
//                .setDrawable(R.drawable.sceen1) // int top drawable
//                .setSummary("")
//                .build());
//
//        addFragment(new Step.Builder().setTitle("This is header 2")
//                .setContent("This is content")
//                .setBackgroundColor(Color.parseColor("#cccccc")) // int background color
//                .setDrawable(R.drawable.fashionnn) // int top drawable
//                .setSummary("This is summary")
//                .build());
//
//        addFragment(new Step.Builder().setTitle("This is header 3")
//                .setContent("This is content")
//                .setBackgroundColor(Color.parseColor("#cccccc")) // int background color
//                .setDrawable(R.drawable.rerf) // int top drawable
//                .setSummary("This is summary")
//                .build());
//    }
//
//    @Override
//    public void currentFragmentPosition(int i) {
//
//    }
//}
