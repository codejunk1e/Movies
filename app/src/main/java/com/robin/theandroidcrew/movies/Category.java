package com.robin.theandroidcrew.movies;

enum Category {

    TOPRATED("top_rated"),
    POPULAR("popular");

    private final String name;
    Category(String s) {
        this.name= s;
    }

    public String toString() {
        return this.name;
    }
}
