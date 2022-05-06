package com.keane9301.myapp001.Database.Lubes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LubeViewModel extends AndroidViewModel {
    public static LubeRepository repository;
    public final LiveData<List<LubeEntity>> allLubes;

    public LubeViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new LubeRepository(application);
        allLubes = repository.getAllLubes();
    }

    // Get all from database
    public LiveData<List<LubeEntity>> getAllLubes () { return allLubes; }

    // Insert new to database
    public static void insertLube (LubeEntity lube) {
        repository.insertLube(lube);
    }

    // Delete existing from database
    public static void deleteLube (LubeEntity lube) {
        repository.deleteLube(lube);
    }

    // Update existing from database
    public static void updateLube (LubeEntity lube) {
        repository.updateLube(lube);
    }

    // Get individual from database
    public LiveData<LubeEntity> get (int id) {
        return repository.getLube(id);
    }

    // Search using keyword from database
    public static LiveData<List<LubeEntity>> searchLube (String searchWord) {
        return repository.searchLubes(searchWord);
    }
}
