package com.keane9301.myapp001.Database.Customers;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;


@Entity(tableName = "customer_table")
public class CustomerEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    // Customer name
    private String name;
    // Customer identification number (if any)
    private String identification;
    // Customer shop name
    private String shopName;
    // Customer business registration number (if any)
    private String registration;
    // Customer contact number
    private String number;
    // Customer alternative contact number
    private String numberAlternative;
    // Customer address
    private String address;
    // Customer location based on GPS
    private String latLng;

    // Entry or edit time recorded
    private String lastModified;
    // An image related to the customer
    private byte[] image;


    public CustomerEntity(String name, String identification, String shopName, String registration, String number, String numberAlternative,
                          String address, String latLng, String lastModified, byte[] image) {
        this.name = name;
        this.identification = identification;
        this.shopName = shopName;
        this.registration = registration;
        this.number = number;
        this.numberAlternative = numberAlternative;
        this.address = address;
        this.latLng = latLng;
        this.lastModified = lastModified;
        this.image = image;
    }



    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNumberAlternative(String numberAlternative) {
        this.numberAlternative = numberAlternative;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }


    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getNumberAlternative() {
        return numberAlternative;
    }

    public String getAddress() {
        return address;
    }

    public String getLatLng() {
        return latLng;
    }



    public String getLastModified() {
        return lastModified;
    }
}


