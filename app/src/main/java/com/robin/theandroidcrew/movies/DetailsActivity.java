package com.robin.theandroidcrew.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin.theandroidcrew.movies.model.Movie;
import com.squareup.picasso.Picasso;

import static com.robin.theandroidcrew.movies.MainActivity.MOVIE_KEY;

public class DetailsActivity extends AppCompatActivity {
    private ImageView imagePoster;
    private TextView detailsTitle, releaseDate, rate, overview;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imagePoster = findViewById(R.id.img_poster);
        detailsTitle = findViewById(R.id.tv_details_title);
        releaseDate = findViewById(R.id.tv_releaseDate);
        rate = findViewById(R.id.tv_rate);
        overview = findViewById(R.id.tv_overview);


        Intent intent = getIntent();
        if (intent != null){
            movie = intent.getParcelableExtra(MOVIE_KEY);

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

    }
}