package com.keane9301.myapp001.Database.Utils;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DefaultForEmptyField {

    // When user key in a new product, not every field will be fill up so when user save
    // the item, the empty field will be replaced with default instead of being null

    public static ArrayList<String> insertDefaultForCustomer (String name, String identification,
                                                              String shop, String registration,
                                                              String number, String numberAlt,
                                                              String address, String locLatLng) {

        ArrayList<String> adjustedLists = new ArrayList<>();

        String nameText;
        String identificationText;
        String shopText;
        String registrationText;
        String numberText;
        String numberAltText;
        String addressText;
        String locLatLngText;


        if(name == null || name.isEmpty()) {
            nameText = "N/A";
        } else {
            nameText = name;
        }

        if(identification == null || identification.isEmpty()) {
            identificationText = "N/A";
        } else {
            identificationText = identification;
        }

        if(shop == null || shop.isEmpty()) {
            shopText = "N/A";
        } else {
            shopText = shop;
        }

        if(registration == null || registration.isEmpty()) {
            registrationText = "N/A";
        } else {
            registrationText = registration;
        }

        if(number == null || number.isEmpty()) {
            numberText = "-";
        } else {
            numberText = number;
        }

        if(numberAlt == null || numberAlt.isEmpty()) {
            numberAltText = "-";
        } else {
            numberAltText = numberAlt;
        }

        if(address == null || address.isEmpty()) {
            addressText = "N/A";
        } else {
            addressText = address;
        }

        if(locLatLng == null || locLatLng.isEmpty()) {
            locLatLngText = "N/A";
        } else {
            locLatLngText = locLatLng;
        }



        adjustedLists.add(nameText);
        adjustedLists.add(identificationText);
        adjustedLists.add(shopText);
        adjustedLists.add(registrationText);
        adjustedLists.add(numberText);
        adjustedLists.add(numberAltText);
        adjustedLists.add(addressText);
        adjustedLists.add(locLatLngText);

        return adjustedLists;
    }





    public static ArrayList<String> insertDefaultForPart (String application, String category,
            String subCategory, String position, String brand, String condition, String size,
            String partNumber, String partNumberAlt, String partNumberOE, String warranty,
            String location, String supplier, String cost, String profit, String sell, String note) {

        ArrayList<String> fieldsWithData = new ArrayList<>();
        String applicationText = "";
        String categoryText = "";
        String subCategoryText = "";
        String positionText = "";
        String brandText = "";
        String conditionText = "";
        String sizeText = "";
        String partNumberText = "";
        String partNumberAltText = "";
        String partNumberOEText = "";
        String partWarrantyText = "";
        String locationText = "";
        String supplierText = "";
        String costText = "";
        String profitText = "";
        String sellText = "";
        String noteText = "";

        if(application == null || application.isEmpty()) {
            applicationText = "N/A";
        } else {
            applicationText = application;
        }

        if(category == null || category.isEmpty()) {
            categoryText = "N/A";
        } else {
            categoryText = category;
        }

        if(subCategory == null || subCategory.isEmpty()) {
            subCategoryText = "N/A";
        } else {
            subCategoryText = subCategory;
        }

        if(position == null || position.isEmpty()) {
            positionText = "N/A";
        } else {
            positionText = position;
        }

        if(brand == null || brand.isEmpty()) {
            brandText = "N/A";
        } else {
            brandText = brand;
        }

        if(condition == null || condition.isEmpty()) {
            conditionText = "N/A";
        } else {
            conditionText = condition;
        }

        if(size == null || size.isEmpty()) {
            sizeText = "N/A";
        } else {
            sizeText = size;
        }

        if(partNumber == null || partNumber.isEmpty()) {
            partNumberText = "N/A";
        } else {
            partNumberText = partNumber;
        }

        if(partNumberAlt == null || partNumberAlt.isEmpty()) {
            partNumberAltText = "N/A";
        } else {
            partNumberAltText = partNumberAlt;
        }

        if(partNumberOE == null || partNumberOE.isEmpty()) {
            partNumberOEText = "N/A";
        } else {
            partNumberOEText = partNumberOE;
        }

        if(warranty == null || warranty.isEmpty()) {
            partWarrantyText = "NO";
        } else {
            partWarrantyText = warranty;
        }

        if(location == null || location.isEmpty()) {
            locationText = "N/A";
        } else {
            locationText = location;
        }

        if(supplier == null || supplier.isEmpty()) {
            supplierText = "N/A";
        } else {
            supplierText = supplier;
        }

        if(note == null || note.isEmpty()) {
            noteText = "N/A";
        } else {
            noteText = note;
        }

        if(cost == null || cost.isEmpty()) {
            costText = "0.0";
        } else {
            costText = cost;
        }

        if(profit == null || profit.isEmpty()) {
            profitText = "0.0";
        } else {
            profitText = profit;
        }

        if(sell == null || sell.isEmpty()) {
            sellText = "0.0";
        } else {
            sellText = sell;
        }

        if(!cost.isEmpty() && !sell.isEmpty()) {
            costText = cost;
            sellText = sell;
            profitText = Calculation.calculatePercentage(cost, "0", sell);
        } else if(!cost.isEmpty() && !profit.isEmpty()) {
            costText = cost;
            profitText = profit;
            sellText = Calculation.calculatePercentage(cost, profit, "0");
        }



        fieldsWithData.add(applicationText);
        fieldsWithData.add(categoryText);
        fieldsWithData.add(subCategoryText);
        fieldsWithData.add(positionText);
        fieldsWithData.add(brandText);
        fieldsWithData.add(conditionText);
        fieldsWithData.add(sizeText);
        fieldsWithData.add(partNumberText);
        fieldsWithData.add(partNumberAltText);
        fieldsWithData.add(partNumberOEText);
        fieldsWithData.add(partWarrantyText);
        fieldsWithData.add(locationText);
        fieldsWithData.add(supplierText);
        fieldsWithData.add(costText);
        fieldsWithData.add(profitText);
        fieldsWithData.add(sellText);
        fieldsWithData.add(noteText);


        return fieldsWithData;
    }






    public static ArrayList<String> insertDefaultForLube (String brand, String model, String grade,
            String viscosity, String location, String partNumberOE, String content, String cost,
            String profit, String sell, String note) {
        ArrayList<String> fieldsWithData = new ArrayList<>();
        String brandText = "";
        String modelText = "";
        String gradeText = "";
        String viscosityText = "";
        String locationText = "";
        String partNumberOEText = "";
        String contentText = "";
        String costText = "";
        String profitText = "";
        String sellText = "";
        String noteText = "";

        if(brand.isEmpty()) {
            brandText = "N/A";
        } else {
            brandText = brand;
        }

        if(model.isEmpty()) {
            modelText = "N/A";
        } else {
            modelText = model;
        }

        if(grade == null || grade.isEmpty()) {
            gradeText = "N/A";
        } else {
            gradeText = grade;
        }

        if(viscosity == null || viscosity.isEmpty()) {
            viscosityText = "N/A";
        } else {
            viscosityText = viscosity;
        }

        if(location == null || location.isEmpty()) {
            locationText = "N/A";
        } else {
            locationText = location;
        }

        if(partNumberOE == null || partNumberOE.isEmpty()) {
            partNumberOEText = "N/A";
        } else {
            partNumberOEText = partNumberOE;
        }

        if(content == null || content.isEmpty()) {
            contentText = "N/A";
        } else {
            contentText = content;
        }

        if(cost == null || cost.isEmpty()) {
            costText = "0.0";
        } else {
            costText = cost;
        }

        if(profit == null || profit.isEmpty()) {
            profitText = "0.0";
        } else {
            profitText = profit;
        }

        if(sell == null || sell.isEmpty()) {
            sellText = "0.0";
        } else {
            sellText = sell;
        }

        if(note.isEmpty()) {
            noteText = "N/A";
        } else {
            noteText = note;
        }


        // Calculate profit or sell based on the info received by user
        if(!costText.equals("0.0") && !sellText.equals("0.0")) {
            profitText = Calculation.calculatePercentage(costText, "0.0", sellText);
        } else if(!costText.equals("0.0") && !profitText.equals("0.0")) {
            sellText = Calculation.calculatePercentage(costText, profitText, "0.0");
        }



        fieldsWithData.add(brandText);
        fieldsWithData.add(modelText);
        fieldsWithData.add(gradeText);
        fieldsWithData.add(viscosityText);
        fieldsWithData.add(locationText);
        fieldsWithData.add(partNumberOEText);
        fieldsWithData.add(contentText);
        fieldsWithData.add(costText);
        fieldsWithData.add(profitText);
        fieldsWithData.add(sellText);
        fieldsWithData.add(noteText);



        return fieldsWithData;
    }



}
