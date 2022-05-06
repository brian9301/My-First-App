package com.keane9301.myapp001.ViewsAndAdapters;

import com.keane9301.myapp001.Database.Customers.CustomerEntity;

public interface OnCustomerClickListener {
    void OnCustomerClick(int adapterPosition, CustomerEntity customer);
}
