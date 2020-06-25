package com.robin.theandroidcrew.movies.utils;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Categories {
    public static final String TOP_RATED = "top_rated";
    public static final String POPULAR = "popular";
    public static final String FAVOURITES = "favourites";

    @StringDef({TOP_RATED, POPULAR, FAVOURITES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {}
}
