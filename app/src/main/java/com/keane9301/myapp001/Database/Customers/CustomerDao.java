package com.keane9301.myapp001.Database.Customers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;




@Dao
public interface CustomerDao {

    // Insert new data to database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCustomer (CustomerEntity customerEntity);

    // Delete data from database
    @Delete
    void deleteCustomer (CustomerEntity customerEntity);

    // Update data from database
    @Update
    void updateCustomer (CustomerEntity customerEntity);

    // Get a list of available data
    @Query("SELECT * FROM customer_table ORDER BY name")
    LiveData<List<CustomerEntity>> getAllCustomers();

    // Get a customer by looking for matching id's
    @Query("SELECT * FROM customer_table WHERE customer_table.id == :id")
    LiveData<CustomerEntity> getCustomer (int id);

    // Search database using matching keyword
    // Keywords are non case sensitive
    @Query("SELECT * FROM customer_table WHERE name COLLATE NOCASE LIKE " +
            "('%' || :searchWord || '%') OR shopName COLLATE NOCASE LIKE " +
            "('%' || :searchWord || '%') OR address COLLATE NOCASE LIKE " +
            "('%' || :searchWord)")
    LiveData<List<CustomerEntity>> getSearchCustomer (String searchWord);


}
