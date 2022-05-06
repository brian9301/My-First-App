package com.keane9301.myapp001.Database.Utils;

import android.util.Log;

import com.keane9301.myapp001.ui.gallery.AddEditPart;

public class Calculation {
    public static String calculatePercentage (String cost, String profit, String sell) {
        String data = "";
        // Convert String received into float before calculation is done
        float costing = Float.parseFloat(cost);
        float profiting = Float.parseFloat(profit);
        float selling = Float.parseFloat(sell);

        // Usually user will provide cost and sell so program will calculate profit
        // In some case where user would like to provide cost and profit (in percent)
        // The program will based on user data input and calculate selling price
        if (!cost.equals("0.0") && !sell.equals("0.0")) {
            profiting = ((selling - costing) / costing) * 100;
            data = String.valueOf(profiting) + '%';
        } else if (!cost.equals("0.0") && !profit.equals("0.0")) {
            selling = (costing * profiting) / 100;
            data = String.valueOf(selling);
        }

        Log.d(AddEditPart.TAG, "calculatePercentage: " + data);
        return data;
    }

}
