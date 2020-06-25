package com.robin.theandroidcrew.movies.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.robin.theandroidcrew.movies.database.Repository;
import com.robin.theandroidcrew.movies.model.Movie;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel{
    private Application context;
    public Repository repository;
    public LiveData<List<Movie>> favMovieList = new MutableLiveData<>();


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        repository = new Repository(context);
    }

    public void getAllFavs(){
        favMovieList = repository.getAllMovies();
    }
}
