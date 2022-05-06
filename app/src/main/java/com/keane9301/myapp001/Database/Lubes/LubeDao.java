package com.keane9301.myapp001.Database.Lubes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LubeDao {

    // For adding new lube to database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLube (LubeEntity lube);

    // For deleting existing lube in database
    @Delete
    void deleteLube (LubeEntity lube);

    // For updating existing lube in database
    @Update
    void updateLube (LubeEntity lube);

    // Get a list of lube from database
    @Query("SELECT * FROM lube_table ORDER BY brand, model ASC")
    LiveData<List<LubeEntity>> getAllLubes ();

    // Get lube by matching the id's in database
    @Query("SELECT * FROM lube_table WHERE lube_table.id == :id")
    LiveData<LubeEntity> getLube (int id);

    // Use for searching for a list of specific lube from database by using keyword
    // Keywords are not case sensitive
    @Query("SELECT * FROM lube_table WHERE brand COLLATE NOCASE LIKE " +
            "('%' || :searchWord || '%') OR model COLLATE NOCASE LIKE " +
            "('%' || :searchWord || '%') OR grade COLLATE NOCASE LIKE " +
            "('%' || :searchWord || '%')")
    LiveData<List<LubeEntity>> getSearchLube (String searchWord);


}
