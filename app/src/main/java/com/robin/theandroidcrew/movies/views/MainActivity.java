package com.robin.theandroidcrew.movies.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.robin.theandroidcrew.movies.adapter.MovieAdapter;
import com.robin.theandroidcrew.movies.R;
import com.robin.theandroidcrew.movies.model.Movie;
import com.robin.theandroidcrew.movies.utils.JSONUtils;
import com.robin.theandroidcrew.movies.utils.NetworkUtils;
import com.robin.theandroidcrew.movies.utils.Categories;
import com.robin.theandroidcrew.movies.utils.Categories.Category;
import com.robin.theandroidcrew.movies.viewModels.MainActivityViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.robin.theandroidcrew.movies.utils.Categories.FAVOURITES;
import static com.robin.theandroidcrew.movies.utils.NetworkUtils.isOnline;
import static com.robin.theandroidcrew.movies.utils.Categories.TOP_RATED;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainActivityViewModel mainActivityViewModel;
    public static final String MOVIE_KEY = "com.robin.theandroidcrew.movies.MOVIE_KEY";
    private String category = TOP_RATED;
    private ProgressBar progressBar;
    List<Movie> theMoviesList = new ArrayList<>();
    private TextView errorText;
    private RecyclerView myRecyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorView);
        myRecyclerView = findViewById(R.id.main_recyclerView);


        if (isOnline()){
            errorText.setVisibility(View.INVISIBLE);
            fetchMovieList(category);
        }else {
            errorText.setVisibility(View.VISIBLE);
        }

        movieAdapter = new MovieAdapter(theMoviesList, this);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setAdapter(movieAdapter);
    }

    public void fetchMovieList(@Category String type){
        errorText.setVisibility(View.INVISIBLE);
        if(!type.equals(FAVOURITES)){
            new FetchTask().execute(type);
        }
        else {
            mainActivityViewModel.getAllFavs();
            mainActivityViewModel.favMovieList.observe(this, movies -> {
                theMoviesList = movies;
                movieAdapter = new MovieAdapter(theMoviesList, MainActivity.this);
                myRecyclerView.setAdapter(movieAdapter);
            });

        }
    }

    public class FetchTask extends AsyncTask<String, Void, List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {
            List<Movie> moviesList = new ArrayList<>();

                URL requestURL = NetworkUtils.buildURL(strings[0]);
                try{
                    String response = NetworkUtils.getResponseFromHttpUrl(requestURL);
                    moviesList = JSONUtils.parseMovies(response);
                }
                catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            Log.d(TAG, moviesList.toString());
                return moviesList;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null){
                progressBar.setVisibility(View.INVISIBLE);
                theMoviesList = movies;
                movieAdapter = new MovieAdapter(theMoviesList, MainActivity.this);
                myRecyclerView.setAdapter(movieAdapter);
            }

            else {
                progressBar.setVisibility(View.INVISIBLE);
                errorText.setVisibility(View.VISIBLE);
            }

            super.onPostExecute(movies);
        }
    }

    @Override
    public void onClick(int position) {
        if (theMoviesList != null){

            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(MOVIE_KEY, theMoviesList.get(position));
            startActivity(intent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sort_popular:
                if (!(category.equals(Categories.POPULAR))){
                    category = Categories.POPULAR;
                    fetchMovieList(category);
                }else {
                    Toast.makeText(this, "Already Showing popular movies", Toast.LENGTH_SHORT).show();
                }
                break;
            case  R.id.action_sort_topRted:
                if (!(category.equals(TOP_RATED))){
                    category = TOP_RATED;
                    fetchMovieList(category);
                }else {
                    Toast.makeText(this, "Already Showing top rated movies", Toast.LENGTH_SHORT).show();
                }
                break;
            case  R.id.action_show_favourites:
                if (!(category.equals(FAVOURITES))){
                    category = FAVOURITES;
                    fetchMovieList(category);
                }else {
                    Toast.makeText(this, "Already Showing favourite movies", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}