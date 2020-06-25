package com.robin.theandroidcrew.movies.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.robin.theandroidcrew.movies.model.Movie;
import com.robin.theandroidcrew.movies.utils.AppExecutors;

import java.util.List;

/*
 Single source of truth
*/

public class Repository {

    private final Database database;

    public Repository(Application application) {
        database = Database.getInstance(application);
    }

    public LiveData<List<Movie>> getAllMovies (){
        return database.movieDao().loadAllMovies();
    }

    public void insertIntoDatabase(Movie movie) {
       AppExecutors.getInstance().diskIO().execute(() -> {
           database.movieDao().insertMovie(movie);
       });
    }

    public void deleteFromDatabase(Movie movie) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            database.movieDao().deleteMovie(movie);
        });
    }
}
