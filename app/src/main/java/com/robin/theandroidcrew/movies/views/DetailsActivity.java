package com.robin.theandroidcrew.movies.views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.robin.theandroidcrew.movies.R;
import com.robin.theandroidcrew.movies.adapter.ReviewsAdapter;
import com.robin.theandroidcrew.movies.adapter.TrailerAdapter;
import com.robin.theandroidcrew.movies.model.Movie;
import com.robin.theandroidcrew.movies.model.Review;
import com.robin.theandroidcrew.movies.model.Trailer;
import com.robin.theandroidcrew.movies.utils.JSONUtils;
import com.robin.theandroidcrew.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.robin.theandroidcrew.movies.utils.NetworkUtils.isOnline;
import static com.robin.theandroidcrew.movies.views.MainActivity.MOVIE_KEY;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final String reviews = "419704/reviews";
    private static final String trailers = "238/videos";
    Movie movie;
    List<Review> theReviewList = new ArrayList<>();
    List<Trailer> theTrailerList = new ArrayList<>();
    private ImageView imagePoster;
    private TextView detailsTitle, releaseDate, rate, overview;
    private RecyclerView reviewRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private RecyclerView trailerRecycler;
    private TrailerAdapter trailerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imagePoster = findViewById(R.id.img_poster);
        detailsTitle = findViewById(R.id.tv_details_title);
        releaseDate = findViewById(R.id.tv_releaseDate);
        rate = findViewById(R.id.tv_rate);
        overview = findViewById(R.id.tv_overview);

        reviewRecyclerView = findViewById(R.id.recyclerViewReviews);
        reviewsAdapter = new ReviewsAdapter(theReviewList, this);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setAdapter(reviewsAdapter);

        trailerRecycler = findViewById(R.id.recyclerViewTrailers);
        trailerAdapter = new TrailerAdapter(theTrailerList, this);
        trailerRecycler.setHasFixedSize(true);
        trailerRecycler.setAdapter(trailerAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            movie = intent.getParcelableExtra(MOVIE_KEY);

            if (movie != null) {

                detailsTitle.setText(movie.getTitle());
                releaseDate.setText(movie.getReleaseDate());
                rate.setText(movie.getUserRating());
                overview.setText(movie.getOverview());

                Picasso.get()
                        .load(movie.getPoster())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(imagePoster);

            }

            if (isOnline()) {
                fetchReviwsAndTrailerList(trailers, reviews);
            }
        }
    }

    private void fetchReviwsAndTrailerList(String trailerpath, String reviewPath) {
        new FetchReviews().execute(reviewPath);
        new FetchTrailers().execute(trailerpath);
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