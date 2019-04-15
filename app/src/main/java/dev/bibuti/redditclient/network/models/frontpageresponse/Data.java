package dev.bibuti.redditclient.network.models.frontpageresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data implements Parcelable {

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
    @SerializedName("after")
    @Expose
    private String after;
    @SerializedName("before")
    @Expose
    private String before;
    @SerializedName("children")
    @Expose
    private List<Children> childrenList;

    public Data() {
    }

    protected Data(Parcel in) {
        this.after = in.readString();
        this.before = in.readString();
        this.childrenList = in.createTypedArrayList(Children.CREATOR);
    }

    public String getAfter() {
        return after;
    }

    public String getBefore() {
        return before;
    }

    public List<Children> getChildrenList() {
        return childrenList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.after);
        dest.writeString(this.before);
        dest.writeTypedList(this.childrenList);
    }
}
