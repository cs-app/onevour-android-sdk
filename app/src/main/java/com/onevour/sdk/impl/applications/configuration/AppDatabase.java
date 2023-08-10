package com.onevour.sdk.impl.applications.configuration;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.onevour.sdk.impl.repositories.database.entity.User;
import com.onevour.sdk.impl.repositories.database.repository.UserRepository;

import java.util.Objects;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        init(context);
        return instance;
    }

    public static void init(Context context) {
        if (Objects.isNull(instance)) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "onevour-sdk")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }

    public abstract UserRepository userRepository();

}