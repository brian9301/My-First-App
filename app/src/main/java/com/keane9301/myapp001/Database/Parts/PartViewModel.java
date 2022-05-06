package com.keane9301.myapp001.Database.Parts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PartViewModel extends AndroidViewModel {
    public static PartRepository repository;
    public final LiveData<List<PartEntity>> allParts;

    public PartViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new PartRepository(application);
        allParts = repository.getAllParts();
    }

    // Show all parts from database
    public LiveData<List<PartEntity>> getAllParts () { return allParts; }

    // Store new part into database
    public static void insertPart (PartEntity part) {
        repository.insertPart(part);
    }

    // Delete existing part from database
    public static void deletePart (PartEntity part) {
        repository.deletePart(part);
    }

    // Update existing part in dataabase
    public static void updatePart (PartEntity part) {
        repository.updatePart(part);
    }

    // Get the part by mathing id's
    public LiveData<PartEntity> getPart (int id) {
        return repository.getPart(id);
    }

    // Search for part based on keywords entered
    public LiveData<List<PartEntity>> searchPart (String searchWord) {
        return repository.searchPart(searchWord);
    }
}
