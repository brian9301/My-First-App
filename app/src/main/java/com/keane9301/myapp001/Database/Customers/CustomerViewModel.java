package com.keane9301.myapp001.Database.Customers;

import android.app.Application;
import android.graphics.LightingColorFilter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;



public class CustomerViewModel extends AndroidViewModel {
    public static CustomerRepository repository;
    public final LiveData<List<CustomerEntity>> allCustomers;


    public CustomerViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new CustomerRepository(application);
        allCustomers = repository.getAllCustomers();
    }

    // Get all customers in database
    public LiveData<List<CustomerEntity>> getAllCustomers() { return allCustomers; }

    // Store new customer in database
    public static void insertCustomer (CustomerEntity customerEntity) { repository.insertCustomer(customerEntity); }

    // Delete existing customer
    public static void deleteCustomer (CustomerEntity customerEntity) { repository.deleteCustomer(customerEntity); }

    // Update details of existing customer
    public static void updateCustomer (CustomerEntity customerEntity) { repository.updateCustomer(customerEntity); }

    // Get customer by matching id's
    public LiveData<CustomerEntity> getCustomer (int id) { return repository.getCustomer(id); }

    // Search for customer by looking for keywords
    public LiveData<List<CustomerEntity>> searchCustomer (String searchWord) { return repository.searchCustomer(searchWord); }



}

