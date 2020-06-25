package com.robin.theandroidcrew.movies.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.robin.theandroidcrew.movies.database.Repository;
import com.robin.theandroidcrew.movies.model.Movie;

import java.util.List;

public class DetailsActivityViewModel extends AndroidViewModel {

    private Application context;
    public Repository repository;

    public DetailsActivityViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        repository = new Repository(context);
    }

    public void insert(Movie movie) {
        repository.insertIntoDatabase(movie);
    }

    public void delete(Movie movie) {
        repository.deleteFromDatabase(movie);
    }
}
