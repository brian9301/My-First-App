package com.keane9301.myapp001.Database.Parts;

import android.provider.Telephony;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PartDao {

    // Inserting new data to database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPart (PartEntity part);

    // Deleting data from database
    @Delete
    void deletePart (PartEntity part);

    // Updating data from database
    @Update
    void updatePart (PartEntity part);

    // Getting a list of data from database
    @Query("SELECT * FROM part_table ORDER BY category, application ASC")
    LiveData<List<PartEntity>> getAllParts();

    // Getting part based on matching id's
    @Query("SELECT * FROM part_table WHERE part_table.id == :id")
    LiveData<PartEntity> getPart (int id);

    // Searching database by using keyword to match
    @Query("SELECT * FROM part_table WHERE application COLLATE NOCASE LIKE " +
            "('%' || :searchWord || '%') OR category COLLATE NOCASE LIKE " +
            "('%' || :searchWord || '%') OR subCategory COLLATE NOCASE LIKE " +
            "('%' || :searchWord || '%')")
    LiveData<List<PartEntity>> getSearchPart (String searchWord);
}
