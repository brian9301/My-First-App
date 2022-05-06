package com.keane9301.myapp001.Database.Lubes;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LubeRepository {

    private LubeDao lubeDao;
    private LiveData<List<LubeEntity>> allLubes;

    public LubeRepository (Application application) {
        LubeRoom db = LubeRoom.getDatabase(application);
        lubeDao = db.lubeDao();
        allLubes = lubeDao.getAllLubes();
    }

    // Pass data in list to ViewModel
    public LiveData<List<LubeEntity>> getAllLubes () { return allLubes; }

    // Pass new data to database from ViewModel
    public void insertLube (LubeEntity lube) {
        LubeRoom.databaseWriteExecutor.execute(() -> {
            lubeDao.insertLube(lube);
        });
    }

    // Delete data from database based on ViewModel demand
    public void deleteLube (LubeEntity lube) {
        LubeRoom.databaseWriteExecutor.execute(() -> {
            lubeDao.deleteLube(lube);
        });
    }

    // Update data from database based on ViewModel demand
    public void updateLube (LubeEntity lube) {
        LubeRoom.databaseWriteExecutor.execute(() -> {
            lubeDao.updateLube(lube);
        });
    }

    // Get a lube by matching id's
    public LiveData<LubeEntity> getLube (int id) {
        return lubeDao.getLube(id);
    }

    // Search data from database by getting keyword from ViewModel
    public LiveData<List<LubeEntity>> searchLubes (String searchWord) {
        return lubeDao.getSearchLube(searchWord);
    }


}
