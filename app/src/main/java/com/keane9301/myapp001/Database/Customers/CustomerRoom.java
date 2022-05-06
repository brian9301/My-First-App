package com.keane9301.myapp001.Database.Customers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



///*

@Database(entities = {CustomerEntity.class}, version = 1)
//@TypeConverters({Converters.class})
public abstract class CustomerRoom extends RoomDatabase {

    public abstract CustomerDao customerDao();

    // Set number of fixed thread so it does not run on main thread and slow the app down
    public static final int NUMBER_OF_THREAD = 8;

    // Database name
    public static final String customerDatabase = "customer_database";

    // Setting threads to be used when database is running
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);

    public static volatile CustomerRoom INSTANCE;

    // Check database availability, if not then create new one
    public static CustomerRoom getDatabase (Context context) {
        if (INSTANCE == null) {
            synchronized (CustomerEntity.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CustomerRoom.class,
                            "customer_database").build();
                }
            }
        }
        return INSTANCE;
    }


    // For further expansion of the database in the future
    // Remember to increase database version when doing so and inject this code into getDatabase()
    static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE customer_table ADD COLUMN here TEXT");
        }
    };
















}

// */
