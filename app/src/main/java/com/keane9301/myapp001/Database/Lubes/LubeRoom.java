package com.keane9301.myapp001.Database.Lubes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {LubeEntity.class}, version = 1)
public abstract class LubeRoom extends RoomDatabase {

    public abstract LubeDao lubeDao();

    // Specify number of fixed thread to prevent main thread congestion
    public static final int NUMBER_OF_THREADS = 8;

    // Database name
    public static final String lubeDatabase = "lube_database";

    // Assigning number of threads to be used
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static volatile LubeRoom INSTANCE;

    // Setting up database
    public static LubeRoom getDatabase (Context context) {
        // Check availability, if not, create one
        if (INSTANCE == null) {
            synchronized (LubeRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LubeRoom.class,
                            "lube_database").build();
                }
            }
        }
        return INSTANCE;
    }


    private static final Callback sRoomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                LubeDao lubeDao = INSTANCE.lubeDao();
                lubeDao.getAllLubes();
            });
        }
    };

    // Added when upgrading database version or when new column is added to database
    // Inject this code into getDatabase() to execute (can inject as many as needed)
    static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE lube_table ADD COLUMN here TEXT");
        }
    };
}
