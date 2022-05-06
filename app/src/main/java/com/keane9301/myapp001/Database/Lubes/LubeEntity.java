package com.keane9301.myapp001.Database.Lubes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Setting up items needed to be fill to be save in database
@Entity(tableName = "lube_table")
public class LubeEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    // Brand of lube
    private String brand;
    // Model of lube
    private String model;
    // Grade of lube (eg. Semi Synthetic)
    private String grade;
    // Viscosity of lube (eg. 5W/30 SP)
    private String viscosity;
    // Content in either litre or millilitre
    private String content;
    // Storage location of the lube
    private String location;
    // Costing of the lube
    private String cost;
    // Profit in percentage based on cost and sell
    private String profit;
    // Selling price of the lube
    private String sell;
    // Any additional notes can be stored here
    private String note;
    // An image related to the lube
    private byte[] image;
    // Date the lube was created or last edited
    private String dateModified;


    public LubeEntity(String brand, String model, String grade, String viscosity,
                      String content, String location, String cost, String profit,
                      String sell, String note, byte[] image, String dateModified) {
        this.brand = brand;
        this.model = model;
        this.grade = grade;
        this.viscosity = viscosity;
        this.content = content;
        this.location = location;
        this.cost = cost;
        this.profit = profit;
        this.sell = sell;
        this.note = note;
        this.image = image;
        this.dateModified = dateModified;
    }


    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getGrade() {
        return grade;
    }

    public String getViscosity() {
        return viscosity;
    }

    public String getContent() {
        return content;
    }

    public String getLocation() {
        return location;
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

    public String getNote() {
        return note;
    }

    public byte[] getImage() {
        return image;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setViscosity(String viscosity) {
        this.viscosity = viscosity;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public void setNote(String note) {
        this.note = note;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
}
