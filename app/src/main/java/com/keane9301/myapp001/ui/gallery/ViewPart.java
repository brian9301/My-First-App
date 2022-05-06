package com.keane9301.myapp001.ui.gallery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.keane9301.myapp001.Database.Parts.PartEntity;
import com.keane9301.myapp001.Database.Parts.PartViewModel;
import com.keane9301.myapp001.Database.Utils.Conversion;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.TranslateApi;
import com.keane9301.myapp001.databinding.ActivityViewPartBinding;
import com.keane9301.myapp001.ui.home.AddEditLube;
import com.keane9301.myapp001.ui.home.ViewLube;
//import com.memetix.mst.language.Language;
//import com.memetix.mst.translate.Translate;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executor;

import javax.xml.transform.sax.SAXResult;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewPart extends AppCompatActivity {
    private ActivityViewPartBinding viewPartBinding;

    public static String ID_TAG = "idTag";
    public static String APPLICATION_TAG = "applicationTag";
    public static String CATEGORY_TAG = "categoryTag";
    public static String SUBCATEGORY_TAG = "subCategoryTag";
    public static String POSITION_TAG = "positionTag";
    public static String BRAND_TAG = "brandTag";
    public static String CONDITION_TAG = "conditionTag";
    public static String SIZE_TAG = "sizeTag";
    public static String PART_NUMBER_TAG = "partNumberTag";
    public static String PART_NUMBER_ALT_TAG = "partNumberAltTag";
    public static String PART_NUMBER_OE_TAG = "partNumberOETag";
    public static String PART_WARRANTY_TAG = "partWarrantyTag";
    public static String LOCATION_TAG = "locationTag";
    public static String SUPPLIER_TAG = "supplierTag";
    public static String COST_TAG = "costTag";
    public static String PROFIT_TAG = "profitTag";
    public static String SELL_TAG = "sellTag";
    public static String NOTE_TAG = "noteTag";
    public static String IMAGE_TAG = "imageTag";
    public static String TIME_TAG = "timeTag";

    private TextView lastModifiedTextView;
    private TextView applicationTextView;
    private TextView categoryTextView;
    private TextView subCategoryTextView;
    private TextView positionTextView;
    private TextView brandTextView;
    private TextView conditionTextView;
    private TextView sizeTextView;
    private TextView partNumberTextView;
    private TextView partNumberAltTextView;
    private TextView partNumberOETextView;
    private TextView partWarrantyTextView;
    private TextView locationTextView;
    private TextView costTitle;
    private TextView costTextView;
    private TextView profitTextView;
    private TextView sellTextView;
    private TextView noteTextView;
    private ImageView imageView;
    private ImageButton translator;
    private Button edit;
    private Button delete;

    private int id;
    private String application;
    private String category;
    private String subCategory;
    private String position;
    private String supplier;
    private String brand;
    private String condition;
    private String size;
    private String partNumber;
    private String partNumberAlt;
    private String partNumberOE;
    private String partWarranty;
    private String location;
    private String cost;
    private String profit;
    private String sell;
    private String note;
    private byte[] byteImage;
    private String time;





    // Getting back data after user edit the part and update the old data with new data
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        assert intent != null;
                        id = intent.getIntExtra(ID_TAG, 0);
                        application = intent.getStringExtra(APPLICATION_TAG);
                        category = intent.getStringExtra(CATEGORY_TAG);
                        subCategory = intent.getStringExtra(SUBCATEGORY_TAG);
                        position = intent.getStringExtra(POSITION_TAG);
                        brand = intent.getStringExtra(BRAND_TAG);
                        condition = intent.getStringExtra(CONDITION_TAG);
                        size = intent.getStringExtra(SIZE_TAG);
                        partNumber = intent.getStringExtra(PART_NUMBER_TAG);
                        partNumberAlt = intent.getStringExtra(PART_NUMBER_ALT_TAG);
                        partNumberOE = intent.getStringExtra(PART_NUMBER_OE_TAG);
                        partWarranty = intent.getStringExtra(PART_WARRANTY_TAG);
                        location = intent.getStringExtra(LOCATION_TAG);
                        supplier = intent.getStringExtra(SUPPLIER_TAG);
                        cost = intent.getStringExtra(COST_TAG);
                        profit = intent.getStringExtra(PROFIT_TAG);
                        sell = intent.getStringExtra(SELL_TAG);
                        note = intent.getStringExtra(NOTE_TAG);
                        byteImage = intent.getByteArrayExtra(IMAGE_TAG);
                        time = intent.getStringExtra(TIME_TAG);

                        Log.d(AddEditPart.TAG, "onActivityResult: " + application + ' ' + category + ' ' + condition);

                        PartEntity partEntity = new PartEntity(application, category, subCategory,
                                position, brand, condition, size, partNumber, partNumberAlt, partNumberOE,
                                partWarranty, location, supplier, note, byteImage, cost, profit, sell, time);

                        // Set the id back to the original is important or else it will create a
                        // New id for the item and update will throw an error as id does not match
                        if(id != 0) {
                            partEntity.setId(id);
                        }
                        PartViewModel.updatePart(partEntity);
                    }
                }
            });




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_part);

        // Disable screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Wire up all buttons and edit texts
        viewPartBinding = DataBindingUtil.setContentView(ViewPart.this, R.layout.activity_view_part);

        lastModifiedTextView = viewPartBinding.lastModTextView;
        applicationTextView = viewPartBinding.applicationTextField;
        categoryTextView = viewPartBinding.categoryTextField;
        subCategoryTextView = viewPartBinding.subCategoryTextField;
        positionTextView = viewPartBinding.positionTextField;
        brandTextView = viewPartBinding.brandPartTextField;
        conditionTextView = viewPartBinding.conditionPartTextField;
        sizeTextView = viewPartBinding.sizePartTextField;
        partNumberTextView = viewPartBinding.partNumberPartTextField;
        partNumberAltTextView = viewPartBinding.partNumberAltPartTextField;
        partNumberOETextView = viewPartBinding.oePartNumberPartTextField;
        partWarrantyTextView = viewPartBinding.partWarrantyTextField;
        locationTextView = viewPartBinding.locationPartTextField;
        costTextView = viewPartBinding.costPartTextField;
        sellTextView = viewPartBinding.sellPartTextField;
        noteTextView = viewPartBinding.notePartTextField;
        costTitle = viewPartBinding.costPartTitleField;

        translator = viewPartBinding.imageButtonTranslator;
        imageView = viewPartBinding.imageViewPart;
        edit = viewPartBinding.editPartButton;
        delete = viewPartBinding.deletePartButton;




        // Fill up data to their fields
        lastModifiedTextView.setText(getIntent().getStringExtra(AddEditPart.TIME_TAG));
        applicationTextView.setText(getIntent().getStringExtra(AddEditPart.APPLICATION_TAG));
        categoryTextView.setText(getIntent().getStringExtra(AddEditPart.CATEGORY_TAG));
        subCategoryTextView.setText(getIntent().getStringExtra(AddEditPart.SUBCATEGORY_TAG));
        positionTextView.setText(getIntent().getStringExtra(AddEditPart.POSITION_TAG));
        brandTextView.setText(getIntent().getStringExtra(AddEditPart.BRAND_TAG));
        conditionTextView.setText(getIntent().getStringExtra(AddEditPart.CONDITION_TAG));
        sizeTextView.setText(getIntent().getStringExtra(AddEditPart.SIZE_TAG));
        partNumberTextView.setText(getIntent().getStringExtra(AddEditPart.PART_NUMBER_TAG));
        partNumberAltTextView.setText(getIntent().getStringExtra(AddEditPart.PART_NUMBER_ALT_TAG));
        partNumberOETextView.setText(getIntent().getStringExtra(AddEditPart.PART_NUMBER_OE_TAG));
        partWarrantyTextView.setText(getIntent().getStringExtra(AddEditPart.PART_WARRANTY_TAG));
        locationTextView.setText(getIntent().getStringExtra(AddEditPart.LOCATION_TAG));
        String newCost = Conversion.fromFloat(getIntent().getStringExtra(AddEditPart.COST_TAG));
        costTitle.setVisibility(View.GONE);
        costTextView.setVisibility(View.GONE);
        costTextView.setText(newCost);
        sellTextView.setText(getIntent().getStringExtra(AddEditPart.SELL_TAG) + " ---> (" + newCost + ')');
        noteTextView.setText(getIntent().getStringExtra(AddEditPart.NOTE_TAG));
        if(getIntent().getByteArrayExtra(AddEditPart.IMAGE_TAG) != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(getIntent().getByteArrayExtra(AddEditPart.IMAGE_TAG));
            Bitmap photo = BitmapFactory.decodeStream(bis);
            imageView.setImageBitmap(photo);
        }




        // Set up with local variable when intent contains data
        id = getIntent().getIntExtra(ID_TAG, 0);
        application = getIntent().getStringExtra(AddEditPart.APPLICATION_TAG);
        category = getIntent().getStringExtra(AddEditPart.CATEGORY_TAG);
        subCategory = getIntent().getStringExtra(AddEditPart.SUBCATEGORY_TAG);
        position = getIntent().getStringExtra(AddEditPart.POSITION_TAG);
        supplier = getIntent().getStringExtra(AddEditPart.SUPPLIER_TAG);
        brand = getIntent().getStringExtra(AddEditPart.BRAND_TAG);
        condition = getIntent().getStringExtra(AddEditPart.CONDITION_TAG);
        size = getIntent().getStringExtra(AddEditPart.SIZE_TAG);
        partNumber = getIntent().getStringExtra(AddEditPart.PART_NUMBER_TAG);
        partNumberAlt = getIntent().getStringExtra(AddEditPart.PART_NUMBER_ALT_TAG);
        partNumberOE = getIntent().getStringExtra(AddEditPart.PART_NUMBER_OE_TAG);
        partWarranty = getIntent().getStringExtra(AddEditPart.PART_WARRANTY_TAG);
        location = getIntent().getStringExtra(AddEditPart.LOCATION_TAG);
        cost = getIntent().getStringExtra(AddEditPart.COST_TAG);
        profit = getIntent().getStringExtra(AddEditPart.PROFIT_TAG);
        sell = getIntent().getStringExtra(AddEditPart.SELL_TAG);
        note = getIntent().getStringExtra(AddEditPart.NOTE_TAG);
        byteImage = getIntent().getByteArrayExtra(AddEditPart.IMAGE_TAG);
        time = getIntent().getStringExtra(AddEditPart.TIME_TAG);








        // Creating variable for BiometricManager
        BiometricManager biometricManager = BiometricManager.from(this);
        switch(biometricManager.canAuthenticate()) {
            // This means biometric sensor is enabled
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(ViewPart.this, "Login via fingerprint or key in password", Toast.LENGTH_SHORT).show();
                break;
            // This means biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(ViewPart.this, "Biometric currently not available", Toast.LENGTH_LONG).show();
                break;
            // This means there is no biometric support for the device
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(ViewPart.this, "Biometric not supported on your device", Toast.LENGTH_SHORT).show();
                break;
            // This means biometric sensor does not contain any fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(ViewPart.this, "No saved fingerprint found", Toast.LENGTH_LONG).show();
                break;
        }



        // Creating variable for executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // Result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(ViewPart.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Gather info needed to be pass to the edit page
                Toast.makeText(ViewPart.this, "Login successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ViewPart.this, AddEditPart.class);
                intent.putExtra(ID_TAG, id);
                intent.putExtra(AddEditPart.APPLICATION_TAG, application);
                intent.putExtra(AddEditPart.CATEGORY_TAG, category);
                intent.putExtra(AddEditPart.SUBCATEGORY_TAG, subCategory);
                intent.putExtra(AddEditPart.POSITION_TAG, position);
                intent.putExtra(AddEditPart.BRAND_TAG, brand);
                intent.putExtra(AddEditPart.CONDITION_TAG, condition);
                intent.putExtra(AddEditPart.SIZE_TAG, size);
                intent.putExtra(AddEditPart.PART_NUMBER_TAG, partNumber);
                intent.putExtra(AddEditPart.PART_NUMBER_ALT_TAG, partNumberAlt);
                intent.putExtra(AddEditPart.PART_NUMBER_OE_TAG, partNumberOE);
                intent.putExtra(AddEditPart.PART_WARRANTY_TAG, partWarranty);
                intent.putExtra(AddEditPart.LOCATION_TAG, location);
                intent.putExtra(AddEditPart.SUPPLIER_TAG, supplier);
                intent.putExtra(AddEditPart.COST_TAG, cost);
                intent.putExtra(AddEditPart.PROFIT_TAG, profit);
                intent.putExtra(AddEditPart.SELL_TAG, sell);
                intent.putExtra(AddEditPart.NOTE_TAG, note);
                if(byteImage != null) {
                    intent.putExtra(AddEditPart.IMAGE_TAG, byteImage);
                } else {
                    intent.putExtra(AddEditPart.IMAGE_TAG, (byte[]) null);
                }
                intent.putExtra(AddEditPart.TIME_TAG, time);
                mStartForResult.launch(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });



        // Creating final variable for prompt
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Hardware found")
                .setDescription("Use your fingerprint to login").setNegativeButtonText("Cancel").build();

        // Biometric prompt dialog
//        biometricPrompt.authenticate(promptInfo);





        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });







        // Creating variable for executor
        Executor executor2 = ContextCompat.getMainExecutor(this);
        // Result of AUTHENTICATION
        final BiometricPrompt biometricPrompt2 = new BiometricPrompt(ViewPart.this, executor2, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Set up the part to be deleted if authentication is successful
                PartEntity partEntity= new PartEntity(application, category, subCategory, position, brand, condition,
                        size, partNumber, partNumberAlt, partNumberOE, partWarranty, location, supplier, note,
                        byteImage, cost, profit, sell, time);
                partEntity.setId(id);
                PartViewModel.deletePart(partEntity);
                Toast.makeText(ViewPart.this, "Part successfully deleted", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });





        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt2.authenticate(promptInfo);
            }
        });



        // Enlarge and enable zoom when image is clicked
        if(getIntent().getByteArrayExtra(IMAGE_TAG) != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(ViewPart.this);
                    dialog.setContentView(R.layout.simple_camera);

                    ImageView imageEnlarge = dialog.findViewById(R.id.enlarge_image);

                    // Get the applied image from imageView as a bitmap
                    Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    // Set bitmap to new imageView
                    imageEnlarge.setImageBitmap(image);
                    imageEnlarge.setAdjustViewBounds(true);
                    PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageEnlarge);

                    photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    photoViewAttacher.setZoomable(true);
                    photoViewAttacher.update();

                    dialog.show();
                }
            });
        }






        // Translate location info to other languages
        if(location != null && !location.isEmpty() && !location.equals("N/A")) {
            translator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TranslateApi translate = new TranslateApi();
                    translate.setOnTranslationCompleteListener(new TranslateApi.OnTranslationCompleteListener() {
                        @Override
                        public void onStartTranslation() {
                            // here you can perform initial work before translated the text like displaying progress bar
                        }

                        @Override
                        public void onCompleted(String text) {
                            // "text" variable will give you the translated text
                            locationTextView.setText(text);
                            Log.d("DEBUG_TAG", "onCompleted: " + location + " --> " + text);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                    // translate.execute(text.getText().toString(),fromLangCode.getText().toString(),toLangCode.getText().toString());
                    // Check the code for your language under TranslateApi.java class
                    translate.execute(location, "en", "zh-Hans");
                }
            });


            translator.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TranslateApi translateApi = new TranslateApi();
                    translateApi.setOnTranslationCompleteListener(new TranslateApi.OnTranslationCompleteListener() {
                        @Override
                        public void onStartTranslation() {
                            // here you can perform initial work before translated the text like displaying progress bar
                        }

                        @Override
                        public void onCompleted(String text) {
                            // "text" variable will give you the translated text
                            locationTextView.setText(text);
                            Log.d("DEBUG_TAG", "onCompleted: " + location + " --> " + text);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                    // translate.execute(text.getText().toString(),fromLangCode.getText().toString(),toLangCode.getText().toString());
                    // Check for the code of for the supported language in TranslateApi.java class
                    translateApi.execute(location, "en", "ms");
                    return false;
                }
            });


            locationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationTextView.setText(location);
                }
            });
        }

    }



}