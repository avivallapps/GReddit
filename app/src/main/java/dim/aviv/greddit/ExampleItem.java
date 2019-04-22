package dim.aviv.greddit;

/**
 * Created by אביב on 10/03/2019.
 */

public class ExampleItem {

    private String mImageUrl;
    private String mCreator;
    private String mLikes;

    public ExampleItem(String mImageUrl, String mCreator, String mLikes) {
        this.mImageUrl = mImageUrl;
        this.mCreator = mCreator;
        this.mLikes = mLikes;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmCreator() {
        return mCreator;
    }

    public String getmLikes() {
        return mLikes;
    }
}
