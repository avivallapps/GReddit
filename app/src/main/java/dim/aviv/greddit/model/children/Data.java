package dim.aviv.greddit.model.children;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by אביב on 09/03/2019.
 */

public class Data {



    @SerializedName("contest_mode")
    @Expose
    private String contest_mode;

    @SerializedName("subreddit")
    @Expose
    private String subreddit;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("permalink")
    @Expose
    private String permalink;

    @SerializedName("created_utc")
    @Expose
    private String created_utc;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("body")
    @Expose
    private String body;



    public String getContest_mode() {
        return contest_mode;
    }

    public void setContest_mode(String contest_mode) {
        this.contest_mode = contest_mode;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPermalink() {
        return "https://www.reddit.com" + permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getCreated_utc() {
        return created_utc;
    }

    public void setCreated_utc(String created_utc) {
        this.created_utc = created_utc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Data{" +
                "contest_mode='" + contest_mode + '\'' +
                ", subreddit='" + subreddit + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", permalink='" + permalink + '\'' +
                ", created_utc='" + created_utc + '\'' +
                ", name='" + name + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
