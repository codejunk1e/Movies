package com.robin.theandroidcrew.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_movie_table")
public class Movie implements Parcelable {
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
    @PrimaryKey
    private int movieID;
    private String title;
    private String poster;
    private String overview;
    private String releaseDate;
    private double userRating;

    public Movie(int movieID, String title, String poster, String overview, String releaseDate, double userRating) {
        this.movieID = movieID;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
    }

    protected Movie(Parcel in) {
        movieID = in.readInt();
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        userRating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieID);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeDouble(userRating);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }
    public String getUserRatingAsString() {
        return Double.toString(userRating);
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

}

