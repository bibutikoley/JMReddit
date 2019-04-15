package dev.bibuti.redditclient.network.models.frontpageresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Children implements Parcelable {

    public static final Parcelable.Creator<Children> CREATOR = new Parcelable.Creator<Children>() {
        @Override
        public Children createFromParcel(Parcel source) {
            return new Children(source);
        }

        @Override
        public Children[] newArray(int size) {
            return new Children[size];
        }
    };
    @SerializedName("data")
    @Expose
    private ChildrenData childrenData;

    public Children() {
    }

    protected Children(Parcel in) {
        this.childrenData = in.readParcelable(ChildrenData.class.getClassLoader());
    }

    public ChildrenData getChildrenData() {
        return childrenData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.childrenData, flags);
    }
}
