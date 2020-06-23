package com.robin.theandroidcrew.movies.utils;
import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Path {
    public static final String TOPRATED = "top_rated";
    public static final String POPULAR = "popular";

    @StringDef({TOPRATED, POPULAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category {}
}
