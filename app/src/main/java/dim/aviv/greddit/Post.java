package dim.aviv.greddit;

/**
 * Created by אביב on 10/03/2019.
 */

public class Post {

    private String title;
    private String author;
   private String date_updated;
    private String postURL;
    private String thumbnailURL;
    private String name;


    public Post(String title, String author,String thumbnailURL, String postURL,String date_updated, String name) {
        this.title = title;
        this.author = author;
        this.thumbnailURL = thumbnailURL;
        this.postURL = postURL;
        this.date_updated = date_updated;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }
}
