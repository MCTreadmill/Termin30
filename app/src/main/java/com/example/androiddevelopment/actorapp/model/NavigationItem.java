package com.example.androiddevelopment.actorapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Luke Skywalker on 28.11.2017.
 */

public class NavigationItem implements Parcelable {

    private String title;
    private String description;
    private int icon;

    public NavigationItem(String title, String description, int icon) {
        this.title = title;
        this.description = description;
        this.icon = icon;
    }

    protected NavigationItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        icon = in.readInt();
    }

    public static final Creator<NavigationItem> CREATOR = new Creator<NavigationItem>() {
        @Override
        public NavigationItem createFromParcel(Parcel in) {
            return new NavigationItem(in);
        }

        @Override
        public NavigationItem[] newArray(int size) {
            return new NavigationItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(description);
        out.writeInt(icon);
    }
}
