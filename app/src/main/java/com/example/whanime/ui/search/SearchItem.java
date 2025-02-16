package com.example.whanime.ui.search;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

// Entity class representing a search item in the database
@Entity(tableName = "search_items")
public class SearchItem implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id; // Primary key for the entity
    public String image; // URL of the image
    public String filename; // Name of the file
    public String episode; // Episode information
    public String video; // URL of the video

    // Public constructor matching the fields
    public SearchItem(int id, String image, String filename, String episode, String video) {
        this.id = id;
        this.image = image;
        this.filename = filename;
        this.episode = episode;
        this.video = video;
    }

    // New constructor without id
    @Ignore
    public SearchItem(String image, String filename, String episode, String video) {
        this.image = image;
        this.filename = filename;
        this.episode = episode;
        this.video = video;
    }

    // Parcelable implementation
    protected SearchItem(Parcel in) {
        id = in.readInt();
        image = in.readString();
        filename = in.readString();
        episode = in.readString();
        video = in.readString();
    }

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @Override
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(filename);
        dest.writeString(episode);
        dest.writeString(video);
    }

    // Getters and setters
    public int getId() {
        return id;
    }
    public String getImage() {
        return image;
    }
    public String getName() {
        return filename;
    }
    public String getEpisode() {
        return episode;
    }
    public String getVideo() {
        return video;
    }
}