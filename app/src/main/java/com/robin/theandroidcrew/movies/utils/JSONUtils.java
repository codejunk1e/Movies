package com.robin.theandroidcrew.movies.utils;
import com.robin.theandroidcrew.movies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    private  static final String MOVIE_RESULTS = "results";
    private  static final String MOVIE_TITLE = "title";
    private  static final String MOVIE_POSTER = "poster_path";
    private  static final String MOVIE_OVERVIEW = "overview";
    private  static final String MOVIE_RELEASE_DATE= "release_date";
    private  static final String MOVIE_RATING= "vote_average";

    private  static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/";
    private  static final String POSTER_SIZE = "w500";

    public static List<Movie> parseJson (String json) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        JSONObject response = new JSONObject(json);
        JSONArray resultsArray = response.getJSONArray(MOVIE_RESULTS);

        for (int i = 0; i < resultsArray.length(); i++){
            movies.add(
                    new Movie(
                            resultsArray.getJSONObject(i).optString(MOVIE_TITLE),
                            POSTER_BASE_URL + POSTER_SIZE + resultsArray.getJSONObject(i).optString(MOVIE_POSTER),
                            resultsArray.getJSONObject(i).optString(MOVIE_OVERVIEW),
                            resultsArray.getJSONObject(i).optString(MOVIE_RELEASE_DATE),
                            resultsArray.getJSONObject(i).optDouble(MOVIE_RATING)
                    )
            );
        }

        return movies;
    }
}
