package com.keane9301.myapp001.Database.Parts;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.keane9301.myapp001.Database.Lubes.LubeRoom;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PartEntity.class}, version = 1)
public abstract class PartRoom extends RoomDatabase {

    public abstract PartDao partDao();

    // Specify number of threads to prevent congestion in main
    public static final int NUMBER_OF_THREADS = 16;

    // Database name
    public static final String partDatabase = "part_database";

    // Setting threads to be used when database is running
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static volatile PartRoom INSTANCE;

    // Check database availability, if not then create new one
    public static PartRoom getDatabase (Context context) {
        if (INSTANCE == null) {
            synchronized (PartRoom.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PartRoom.class,
                            "part_database").build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback mRoomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                PartDao partDao = INSTANCE.partDao();
                partDao.getAllParts();
            });
        }
    };

    // Injected when making changes to existing database by increasing version number
    // Can insert as much as needed
    static Migration Migration_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE part_table ADD COLUMN here TEXT");
        }
    };
}
