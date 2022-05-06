package com.keane9301.myapp001.Database.Parts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Setting up items to be store and creating table for database
@Entity(tableName = "part_table")
public class PartEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    // Compatible model
    private String application;
    // Category of the product (eg. Engine Gasket)
    private String category;
    // Sub-category of the product (eg. Overhaul Set)
    private String subCategory;
    // Where to apply to (eg. Front LH)
    private String position;
    // Brand of the product
    private String brand;
    // Either new, recondition or used
    private String condition;
    // Size or dimension of the product (eg. 109YU25)
    private String size;
    // Part number of the product (usually for aftermarket)
    private String partNumber;
    // Alternative part number of the product (usually for aftermarket)
    private String partNumberAlternative;
    // OE part number that comes from the factory
    private String partNumberOE;
    // Warranty status of the product
    private String warranty;
    // Storage location of the product
    private String location;
    // Supplier of the product
    private String supplier;
    // Product costing
    private String cost;
    // Profit calculated based on costing and selling
    private String profit;
    // Selling price of the product
    private String sell;
    // Additional note to describe the product
    private String note;
    // An image of the product for easy reference
    private byte[] image;
    // Date of creation or last edited
    private String dateModified;


    public PartEntity(String application, String category, String subCategory, String position,
                      String brand, String condition, String size, String partNumber,
                      String partNumberAlternative, String partNumberOE, String warranty, String location,
                      String supplier, String note, byte[] image, String cost, String profit,
                      String sell, String dateModified) {
        this.application = application;
        this.category = category;
        this.subCategory = subCategory;
        this.position = position;
        this.brand = brand;
        this.condition = condition;
        this.size = size;
        this.partNumber = partNumber;
        this.partNumberAlternative = partNumberAlternative;
        this.partNumberOE = partNumberOE;
        this. warranty = warranty;
        this.location = location;
        this.supplier = supplier;
        this.cost = cost;
        this.profit = profit;
        this.sell = sell;
        this.note = note;
        this.image = image;
        this.dateModified = dateModified;
    }


    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getApplication() {
        return application;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getBrand() {
        return brand;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public String getPartNumberAlternative() {
        return partNumberAlternative;
    }

    public String getPartNumberOE() {
        return partNumberOE;
    }

    public String getLocation() {
        return location;
    }

    public String getNote() {
        return note;
    }

    public byte[] getImage() {
        return image;
    }

    public String getCost() {
        return cost;
    }

    public String getProfit() {
        return profit;
    }

    public String getSell() {
        return sell;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setPartNumberAlternative(String partNumberAlternative) {
        this.partNumberAlternative = partNumberAlternative;
    }

    public void setPartNumberOE(String partNumberOE) {
        this.partNumberOE = partNumberOE;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
}
