package dev.bibuti.redditclient.network.models.frontpageresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecureMedia implements Parcelable {

    public static final Parcelable.Creator<SecureMedia> CREATOR = new Parcelable.Creator<SecureMedia>() {
        @Override
        public SecureMedia createFromParcel(Parcel source) {
            return new SecureMedia(source);
        }

        @Override
        public SecureMedia[] newArray(int size) {
            return new SecureMedia[size];
        }
    };
    @SerializedName("reddit_video")
    @Expose
    private RedditVideo redditVideo;

    public SecureMedia() {
    }

    protected SecureMedia(Parcel in) {
        this.redditVideo = in.readParcelable(RedditVideo.class.getClassLoader());
    }

    public RedditVideo getRedditVideo() {
        return redditVideo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.redditVideo, flags);
    }
}
