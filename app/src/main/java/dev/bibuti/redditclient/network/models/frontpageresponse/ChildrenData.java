package dev.bibuti.redditclient.network.models.frontpageresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildrenData implements Parcelable {

    public static final Parcelable.Creator<ChildrenData> CREATOR = new Parcelable.Creator<ChildrenData>() {
        @Override
        public ChildrenData createFromParcel(Parcel source) {
            return new ChildrenData(source);
        }

        @Override
        public ChildrenData[] newArray(int size) {
            return new ChildrenData[size];
        }
    };
    @SerializedName("subreddit")
    @Expose
    private String subreddit;
    @SerializedName("author_fullname")
    @Expose
    private String authorFullname;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subreddit_name_prefixed")
    @Expose
    private String subredditNamePrefixed;
    @SerializedName("thumbnail_height")
    @Expose
    private Integer thumbnailHeight;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("subreddit_type")
    @Expose
    private String subredditType;
    @SerializedName("ups")
    @Expose
    private Integer ups;
    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("thumbnail_width")
    @Expose
    private Integer thumbnailWidth;
    @SerializedName("secure_media")
    @Expose
    private SecureMedia secureMedia; //To Check for Videos, Gifs
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("score")
    @Expose
    private Integer score; //For Votes
    @SerializedName("subreddit_id")
    @Expose
    private String subredditId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("created")
    @Expose
    private Long created; //For date
    @SerializedName("author")
    @Expose
    private String author; //PostedBy
    @SerializedName("num_comments")
    @Expose
    private Integer numComments; //Number of Comments
    @SerializedName("permalink")
    @Expose
    private String permalink; //Reddit Link to Post
    @SerializedName("subreddit_subscribers")
    @Expose
    private Integer subredditSubscribers;
    @SerializedName("url")
    @Expose
    private String url; //To Check if Image or To open in browser
    @SerializedName("is_video")
    @Expose
    private Boolean isVideo;
    @SerializedName("created_utc")
    @Expose
    private Long createdUtc;
    //    @SerializedName("replies")
//    @Expose
//    private Object redditRepiles;
    //Comments
    @SerializedName("body")
    @Expose
    private String commentBody;

    public ChildrenData() {
    }

    protected ChildrenData(Parcel in) {
        this.subreddit = in.readString();
        this.authorFullname = in.readString();
        this.title = in.readString();
        this.subredditNamePrefixed = in.readString();
        this.thumbnailHeight = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.subredditType = in.readString();
        this.ups = (Integer) in.readValue(Integer.class.getClassLoader());
        this.domain = in.readString();
        this.thumbnailWidth = (Integer) in.readValue(Integer.class.getClassLoader());
        this.secureMedia = in.readParcelable(SecureMedia.class.getClassLoader());
        this.thumbnail = in.readString();
        this.score = (Integer) in.readValue(Integer.class.getClassLoader());
        this.subredditId = in.readString();
        this.id = in.readString();
        this.created = (Long) in.readValue(Long.class.getClassLoader());
        this.author = in.readString();
        this.numComments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.permalink = in.readString();
        this.subredditSubscribers = (Integer) in.readValue(Integer.class.getClassLoader());
        this.url = in.readString();
        this.isVideo = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.createdUtc = (Long) in.readValue(Long.class.getClassLoader());
        this.commentBody = in.readString();
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getAuthorFullname() {
        return authorFullname;
    }

    public String getTitle() {
        return title;
    }

    public String getSubredditNamePrefixed() {
        return subredditNamePrefixed;
    }

    public Integer getThumbnailHeight() {
        return thumbnailHeight;
    }

    public String getName() {
        return name;
    }

    public String getSubredditType() {
        return subredditType;
    }

    public Integer getUps() {
        return ups;
    }

    public String getDomain() {
        return domain;
    }

    public SecureMedia getSecureMedia() {
        return secureMedia;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSubredditId() {
        return subredditId;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getNumComments() {
        return numComments;
    }

    public String getPermalink() {
        return permalink;
    }

    public Integer getThumbnailWidth() {
        return thumbnailWidth;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public Long getCreatedUtc() {
        return createdUtc;
    }

    public Integer getScore() {
        return score;
    }

//    public Object getRedditRepiles() {
//        return redditRepiles;
//    }


    @Override
    public int describeContents() {
        return 0;
    }

    public Long getCreated() {
        return created;
    }

    public Integer getSubredditSubscribers() {
        return subredditSubscribers;
    }

    public String getCommentBody() {
        return commentBody;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subreddit);
        dest.writeString(this.authorFullname);
        dest.writeString(this.title);
        dest.writeString(this.subredditNamePrefixed);
        dest.writeValue(this.thumbnailHeight);
        dest.writeString(this.name);
        dest.writeString(this.subredditType);
        dest.writeValue(this.ups);
        dest.writeString(this.domain);
        dest.writeValue(this.thumbnailWidth);
        dest.writeParcelable(this.secureMedia, flags);
        dest.writeString(this.thumbnail);
        dest.writeValue(this.score);
        dest.writeString(this.subredditId);
        dest.writeString(this.id);
        dest.writeValue(this.created);
        dest.writeString(this.author);
        dest.writeValue(this.numComments);
        dest.writeString(this.permalink);
        dest.writeValue(this.subredditSubscribers);
        dest.writeString(this.url);
        dest.writeValue(this.isVideo);
        dest.writeValue(this.createdUtc);
        dest.writeString(this.commentBody);
    }
}
