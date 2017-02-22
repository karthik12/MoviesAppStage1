package com.example.karthikeyanp.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by karthikeyanp on 2/12/2017.
 */

public class Movie implements Parcelable {
    @SerializedName("poster_path")
    String posterPath;
    @SerializedName("adult")
    String adult;
    @SerializedName("overview")
    String overView;
    @SerializedName("release_date")
    String releaseDate;
    @SerializedName("genre_ids")
    int[] genreIds;
    @SerializedName("id")
    int id;
    @SerializedName("original_title")
    String originalTitle;
    @SerializedName("original_language")
    String originalLanguage;
    @SerializedName("title")
    String title;
    @SerializedName("backdrop_path")
    String backdropPath;
    @SerializedName("popularity")
    float popularity;
    @SerializedName("vote_count")
    int voteCount;
    @SerializedName("video")
    boolean video;
    @SerializedName("vote_average")
    float voteAverage;

    protected Movie(Parcel in) {
        posterPath = in.readString();
        adult = in.readString();
        overView = in.readString();
        releaseDate = in.readString();
        genreIds = in.createIntArray();
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readFloat();
        voteCount = in.readInt();
        video = in.readByte() != 0;
        voteAverage = in.readFloat();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(adult);
        dest.writeString(overView);
        dest.writeString(releaseDate);
        dest.writeIntArray(genreIds);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeFloat(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeFloat(voteAverage);
    }
}
