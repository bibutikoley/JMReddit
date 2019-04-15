package dev.bibuti.redditclient.network.models.frontpageresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedditVideo implements Parcelable {

    public static final Parcelable.Creator<RedditVideo> CREATOR = new Parcelable.Creator<RedditVideo>() {
        @Override
        public RedditVideo createFromParcel(Parcel source) {
            return new RedditVideo(source);
        }

        @Override
        public RedditVideo[] newArray(int size) {
            return new RedditVideo[size];
        }
    };
    @SerializedName("fallback_url")
    @Expose
    private String fallbackUrl;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("scrubber_media_url")
    @Expose
    private String scrubberMediaUrl;
    @SerializedName("dash_url")
    @Expose
    private String dashUrl;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("hls_url")
    @Expose
    private String hlsUrl;
    @SerializedName("is_gif")
    @Expose
    private Boolean isGif;
    @SerializedName("transcoding_status")
    @Expose
    private String transcodingStatus;

    public RedditVideo() {
    }

    protected RedditVideo(Parcel in) {
        this.fallbackUrl = in.readString();
        this.height = (Integer) in.readValue(Integer.class.getClassLoader());
        this.width = (Integer) in.readValue(Integer.class.getClassLoader());
        this.scrubberMediaUrl = in.readString();
        this.dashUrl = in.readString();
        this.duration = (Integer) in.readValue(Integer.class.getClassLoader());
        this.hlsUrl = in.readString();
        this.isGif = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.transcodingStatus = in.readString();
    }

    public String getFallbackUrl() {
        return fallbackUrl;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public String getScrubberMediaUrl() {
        return scrubberMediaUrl;
    }

    public String getDashUrl() {
        return dashUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public Boolean getGif() {
        return isGif;
    }

    public String getTranscodingStatus() {
        return transcodingStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fallbackUrl);
        dest.writeValue(this.height);
        dest.writeValue(this.width);
        dest.writeString(this.scrubberMediaUrl);
        dest.writeString(this.dashUrl);
        dest.writeValue(this.duration);
        dest.writeString(this.hlsUrl);
        dest.writeValue(this.isGif);
        dest.writeString(this.transcodingStatus);
    }
}
