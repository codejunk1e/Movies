package com.robin.theandroidcrew.movies.database;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.robin.theandroidcrew.movies.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favourite_movie_table ORDER BY movieID")
    LiveData<List<Movie>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);;
}
