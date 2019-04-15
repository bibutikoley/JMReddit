package dev.bibuti.redditclient.network.models.frontpageresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedditResponse implements Parcelable {

    public static final Parcelable.Creator<RedditResponse> CREATOR = new Parcelable.Creator<RedditResponse>() {
        @Override
        public RedditResponse createFromParcel(Parcel source) {
            return new RedditResponse(source);
        }

        @Override
        public RedditResponse[] newArray(int size) {
            return new RedditResponse[size];
        }
    };
    @SerializedName("data")
    @Expose
    private Data data;

    public RedditResponse() {
    }

    protected RedditResponse(Parcel in) {
        this.data = in.readParcelable(Data.class.getClassLoader());
    }

    public Data getData() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
    }
}
