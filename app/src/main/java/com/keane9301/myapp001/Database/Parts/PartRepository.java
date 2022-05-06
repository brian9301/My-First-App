package com.keane9301.myapp001.Database.Parts;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PartRepository {

    private PartDao partDao;
    private LiveData<List<PartEntity>> allParts;

    public PartRepository (Application application) {
        PartRoom db = PartRoom.getDatabase(application);
        partDao = db.partDao();
        allParts = partDao.getAllParts();
    }

    // Get a list of all available parts
    public LiveData<List<PartEntity>> getAllParts () {
        return allParts;
    }

    // Pass new insert to be done in backend thread
    public void insertPart (PartEntity part) {
        PartRoom.databaseWriteExecutor.execute(() -> {
            partDao.insertPart(part);
        });
    }

    // Delete existing data in database
    public void deletePart (PartEntity part) {
        PartRoom.databaseWriteExecutor.execute(() -> {
            partDao.deletePart(part);
        });
    }

    // Updating existing data in database and workload will be done using backend thread
    public void updatePart (PartEntity part) {
        PartRoom.databaseWriteExecutor.execute(() -> {
            partDao.updatePart(part);
        });
    }

    // Get individual data from database by matching id
    public LiveData<PartEntity> getPart (int id) {
        return partDao.getPart(id);
    }

    // Search for database using specific keyword
    public LiveData<List<PartEntity>> searchPart (String searchWord) {
        return partDao.getSearchPart(searchWord);
    }


}
