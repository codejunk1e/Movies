package com.robin.theandroidcrew.movies.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.robin.theandroidcrew.movies.model.Movie;

@androidx.room.Database(entities = {Movie.class}, exportSchema = false, version = 1)
public abstract class Database extends RoomDatabase {
    private static final String LOG_TAG = Database.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movieDatabase";
    private static Database sInstance;

    public static Database getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        Database.class, Database.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
