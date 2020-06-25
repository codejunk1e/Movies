package com.robin.theandroidcrew.movies.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robin.theandroidcrew.movies.R;
import com.robin.theandroidcrew.movies.adapter.ReviewsAdapter;
import com.robin.theandroidcrew.movies.adapter.TrailerAdapter;
import com.robin.theandroidcrew.movies.model.Movie;
import com.robin.theandroidcrew.movies.model.Review;
import com.robin.theandroidcrew.movies.model.Trailer;
import com.robin.theandroidcrew.movies.utils.JSONUtils;
import com.robin.theandroidcrew.movies.utils.NetworkUtils;
import com.robin.theandroidcrew.movies.viewModels.DetailsActivityViewModel;
import com.robin.theandroidcrew.movies.viewModels.MainActivityViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.robin.theandroidcrew.movies.utils.NetworkUtils.isOnline;
import static com.robin.theandroidcrew.movies.utils.SharedPrefUtils.FAVORITE_KEY;
import static com.robin.theandroidcrew.movies.views.MainActivity.MOVIE_KEY;

public class DetailsActivity extends AppCompatActivity implements ReviewsAdapter.OnClickHandler, TrailerAdapter.OnClickHandler {
    private static final String TAG = DetailsActivity.class.getSimpleName();

    Movie movie;
    List<Review> theReviewList = new ArrayList<>();
    List<Trailer> theTrailerList = new ArrayList<>();
    private ImageView imagePoster;
    private TextView detailsTitle, releaseDate, rate, overview, errorReviews, errorTrailers;
    private RecyclerView reviewRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private RecyclerView trailerRecycler;
    private TrailerAdapter trailerAdapter;
    private ExtendedFloatingActionButton fabFavourites;
    private SharedPreferences preferences;
    String movieKey;
    private DetailsActivityViewModel detailsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imagePoster = findViewById(R.id.img_poster);
        detailsTitle = findViewById(R.id.tv_details_title);
        releaseDate = findViewById(R.id.tv_releaseDate);
        rate = findViewById(R.id.tv_rate);
        overview = findViewById(R.id.tv_overview);
        fabFavourites = findViewById(R.id.acton_add_favourite);
        detailsViewModel = new ViewModelProvider(this).get(DetailsActivityViewModel.class);
        errorReviews = findViewById(R.id.errorReviews);
        errorTrailers = findViewById(R.id.errorTrailers);


        reviewRecyclerView = findViewById(R.id.recyclerViewReviews);
        reviewsAdapter = new ReviewsAdapter(theReviewList, this);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setAdapter(reviewsAdapter);

        trailerRecycler = findViewById(R.id.recyclerViewTrailers);
        trailerAdapter = new TrailerAdapter(theTrailerList, this);
        trailerRecycler.setHasFixedSize(true);
        trailerRecycler.setAdapter(trailerAdapter);
        preferences = getSharedPreferences(FAVORITE_KEY, MODE_PRIVATE);

        Intent intent = getIntent();
        if (intent != null) {
            movie = intent.getParcelableExtra(MOVIE_KEY);

            if (movie != null) {

                detailsTitle.setText(movie.getTitle());
                releaseDate.setText(movie.getReleaseDate());
                rate.setText(movie.getUserRatingAsString());
                overview.setText(movie.getOverview());
                movieKey = String.valueOf(movie.getMovieID());

                Picasso.get()
                        .load(movie.getPoster())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(imagePoster);
            }
            final String reviews = movieKey+"/reviews";
            final String trailers = movieKey+"/videos";

            if (isOnline()) {
                fetchReviwsAndTrailerList(trailers, reviews);
            }
            else {
                errorReviews.setVisibility(View.VISIBLE);
                errorTrailers.setVisibility(View.VISIBLE);
            }

        }


        if (preferences.getBoolean(movieKey, false)) {
            fabFavourites.setIcon(getResources().getDrawable(R.drawable.ic_star_24));
            fabFavourites.setText(R.string.remove_from_favourites);

        } else {
            fabFavourites.setIcon(getResources().getDrawable(R.drawable.ic_star_border_24));
            fabFavourites.setText(R.string.add_to_favourites);
        }

    }

    private void fetchReviwsAndTrailerList(String trailerpath, String reviewPath) {
        new FetchReviews().execute(reviewPath);
        new FetchTrailers().execute(trailerpath);
    }

    public void fabClicked(View view) {

        if (!preferences.getBoolean(movieKey, false)) {
            fabFavourites.setIcon(getResources().getDrawable(R.drawable.ic_star_24));
            fabFavourites.setText(R.string.remove_from_favourites);
            updateValue(true);
            detailsViewModel.insert(movie);

        } else {
            fabFavourites.setIcon(getResources().getDrawable(R.drawable.ic_star_border_24));
            fabFavourites.setText(R.string.add_to_favourites);
            updateValue(false);
            detailsViewModel.delete(movie);
        }
    }

    private void updateValue( Boolean newValue) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(movieKey, newValue);
        editor.apply();
    }

    @Override
    public void onClickReview(int position) {
    }

    @Override
    public void onClickTrailer(int position) {
        Trailer trailer = theTrailerList.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW );
        intent.setData(Uri.parse("https://www.youtube.com/watch?v="+trailer.getKey()));
        startActivity(intent);
    }


    class FetchReviews extends AsyncTask<String, Void, List<Review>> {
        @Override
        protected List<Review> doInBackground(String... strings) {
            List<Review> reviews = new ArrayList<>();
            URL requestURL = NetworkUtils.buildURL(strings[0]);
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(requestURL);
                reviews = JSONUtils.parseReviews(response);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, reviews.toString());
            return reviews;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);
            theReviewList = reviews;
            reviewsAdapter = new ReviewsAdapter(theReviewList, DetailsActivity.this);
            reviewRecyclerView.setAdapter(reviewsAdapter);
        }
    }

    class FetchTrailers extends AsyncTask<String, Void, List<Trailer>> {
        @Override
        protected List<Trailer> doInBackground(String... strings) {
            List<Trailer> trailers = new ArrayList<>();
            URL requestURL = NetworkUtils.buildURL(strings[0]);
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(requestURL);
                trailers = JSONUtils.parseTrailers(response);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, trailers.toString());
            return trailers;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);
            theTrailerList = trailers;
            trailerAdapter = new TrailerAdapter(theTrailerList, DetailsActivity.this);
            trailerRecycler.setAdapter(trailerAdapter);
        }
    }
}