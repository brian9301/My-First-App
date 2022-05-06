package com.keane9301.myapp001.Database.Customers;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.keane9301.myapp001.ViewsAndAdapters.CustomerRecyclerViewAdapter;

import java.util.List;



public class CustomerRepository {

    private CustomerDao customerDao;
    private LiveData<List<CustomerEntity>> allCustomers;

    public CustomerRepository (Application application) {
        CustomerRoom db = CustomerRoom.getDatabase(application.getApplicationContext());
        customerDao = db.customerDao();
        allCustomers = customerDao.getAllCustomers();
    }

    // Get a list of all available customers in database
    public LiveData<List<CustomerEntity>> getAllCustomers() { return allCustomers; }

    // Pass new data to be added to database using backend thread
    public void insertCustomer (CustomerEntity customerEntity) {
        CustomerRoom.databaseWriteExecutor.execute(() -> {
            customerDao.insertCustomer(customerEntity);
        });
    }


    // Delete existing data in database
    public void deleteCustomer (CustomerEntity customerEntity) {
        CustomerRoom.databaseWriteExecutor.execute(() -> {
            customerDao.deleteCustomer(customerEntity);
        });
    }


    // Updating existing data in database and workload will be done using backend thread
    public void updateCustomer (CustomerEntity customerEntity) {
        CustomerRoom.databaseWriteExecutor.execute(() -> {
            customerDao.updateCustomer(customerEntity);
        });
    }

    // Get individual data from database by matching id
    public LiveData<CustomerEntity> getCustomer (int id) { return customerDao.getCustomer(id); }

    // Search for database using specific keyword
    public LiveData<List<CustomerEntity>> searchCustomer (String searchWord) {
        return customerDao.getSearchCustomer(searchWord);
    }



}
