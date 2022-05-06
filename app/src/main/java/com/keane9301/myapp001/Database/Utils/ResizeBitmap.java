package com.keane9301.myapp001.Database.Utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class ResizeBitmap {

    // Downsizing all images to fit into Room database (<1MB with all user data including image)
    public static byte[] downsizeBitmapImageArray (Bitmap fullsize) {
        byte[] downsizedArray;

        // Checking height and width making sure if its more than or less than
        if(fullsize.getHeight() > 825 && fullsize.getWidth() > 1100) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // Set max height and max width of image when exceed the limit
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(fullsize, 1095, 822, false);
            // Compression is done to further save file size then saving to database
            // And output the final product to byte array
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            downsizedArray = bos.toByteArray();
        } else if(fullsize.getWidth() > 825 && fullsize.getHeight() > 1100) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // Setting height and width to the full size image
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(fullsize, 822, 1095, false);
            // Compress the image further to save size and output to byte array
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            downsizedArray = bos.toByteArray();
        } else {
            // When its less than the specified range, save the image as it is
            ByteArrayOutputStream bosNoChange = new ByteArrayOutputStream();
            fullsize.compress(Bitmap.CompressFormat.JPEG, 90, bosNoChange);
            downsizedArray = bosNoChange.toByteArray();
        }

        return downsizedArray;
    }
}
