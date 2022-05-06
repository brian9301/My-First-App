package com.keane9301.myapp001.ui.home;

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
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.keane9301.myapp001.Database.Lubes.LubeEntity;
import com.keane9301.myapp001.Database.Lubes.LubeViewModel;
import com.keane9301.myapp001.Database.Utils.Conversion;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.TranslateApi;
import com.keane9301.myapp001.databinding.ActivityViewLubeBinding;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executor;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewLube extends AppCompatActivity {
    private ActivityViewLubeBinding viewLubeBinding;

    public static String ID_TAG = "idTag";
    public static String BRAND_TAG = "brandTag";
    public static String MODEL_TAG = "modelTag";
    public static String GRADE_TAG = "gradeTag";
    public static String VISCOSITY_TAG = "viscosityTag";
    public static String LOCATION_TAG = "locationTag";
    public static String PART_NUMBER_OE_TAG = "partNumberOETag";
    public static String CONTENT_TAG = "contentTag";
    public static String COST_TAG = "costTag";
    public static String PROFIT_TAG = "profitTag";
    public static String SELL_TAG = "sellTag";
    public static String NOTE_TAG = "noteTag";
    public static String IMAGE_TAG = "imageTag";
    public static String TIME_TAG = "timeTag";

    private TextView lastModifiedTextView;
    private TextView brandTextView;
    private TextView modelTextView;
    private TextView gradeTextView;
    private TextView viscosityTextView;
    private TextView locationTextView;
    private TextView partNumberOETextView;
    private TextView contentTextView;
    private TextView costTextView;
    private TextView profitTextView;
    private TextView sellTextView;
    private TextView noteTextView;
    private ImageView lubeImageView;
    private TextView lastModified;
    private ImageButton translator;
    private Button edit;
    private Button delete;

    private int id;
    private String brand;
    private String model;
    private String grade;
    private String viscosity;
    private String location;
    private String partNumberOE;
    private String content;
    private String cost;
    private String profit;
    private String sell;
    private String note;
    private byte[] image;
    private String time;






    // Receiving data to be process in updating existing entry on database
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        brand = intent.getStringExtra(AddEditLube.BRAND_TAG);
                        model = intent.getStringExtra(AddEditLube.MODEL_TAG);
                        grade = intent.getStringExtra(AddEditLube.GRADE_TAG);
                        viscosity = intent.getStringExtra(AddEditLube.VISCOSITY_TAG);
                        location = intent.getStringExtra(AddEditLube.LOCATION_TAG);
                        partNumberOE = intent.getStringExtra(AddEditLube.PART_NUMBER_OE_TAG);
                        content = intent.getStringExtra(AddEditLube.CONTENT_TAG);
                        cost = intent.getStringExtra(AddEditLube.COST_TAG);
                        profit = intent.getStringExtra(AddEditLube.PROFIT_TAG);
                        sell = intent.getStringExtra(AddEditLube.SELL_TAG);
                        note = intent.getStringExtra(AddEditLube.NOTE_TAG);
                        image = intent.getByteArrayExtra(AddEditLube.IMAGE_TAG);
                        time = intent.getStringExtra(AddEditLube.TIME_TAG);

                        LubeEntity lubeEntity = new LubeEntity(brand, model, grade, viscosity, content, location,
                                cost, profit, sell, note, image, time);
                        // id must be set to be equal to the original entry or else update will fail
                        lubeEntity.setId(id);
                        LubeViewModel.updateLube(lubeEntity);
                    }
                }
            });





    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_lube);

        // Disable screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Wire up all buttons and edit texts
        viewLubeBinding = DataBindingUtil.setContentView(ViewLube.this, R.layout.activity_view_lube);

        lastModifiedTextView = viewLubeBinding.modDateTextView;
        brandTextView = viewLubeBinding.brandTextView;
        modelTextView = viewLubeBinding.modelTextView;
        gradeTextView = viewLubeBinding.gradeTextView;
        viscosityTextView = viewLubeBinding.viscosityTextView;
        locationTextView = viewLubeBinding.locationTextView;
        partNumberOETextView = viewLubeBinding.oePartNumberTextView;
        contentTextView = viewLubeBinding.contentTextView;
        costTextView = viewLubeBinding.costTextView;
        sellTextView = viewLubeBinding.sellTextView;
        noteTextView = viewLubeBinding.noteTextView;
        lubeImageView = viewLubeBinding.lubeViewImageView;

        translator = viewLubeBinding.imageButtonTranslator;
        edit = viewLubeBinding.editButton;
        delete = viewLubeBinding.deleteButton;
        lastModified = viewLubeBinding.modDateTextView;




        // Fill up fields with respective data
        lastModifiedTextView.setText(getIntent().getStringExtra(TIME_TAG));
        brandTextView.setText(getIntent().getStringExtra(AddEditLube.BRAND_TAG));
        modelTextView.setText(getIntent().getStringExtra(AddEditLube.MODEL_TAG));
        gradeTextView.setText(getIntent().getStringExtra(AddEditLube.GRADE_TAG));
        viscosityTextView.setText(getIntent().getStringExtra(AddEditLube.VISCOSITY_TAG));
        locationTextView.setText(getIntent().getStringExtra(AddEditLube.LOCATION_TAG));
        partNumberOETextView.setText(getIntent().getStringExtra(AddEditLube.PART_NUMBER_OE_TAG));
        contentTextView.setText(getIntent().getStringExtra(AddEditLube.CONTENT_TAG));
        String newCost = Conversion.fromFloat(getIntent().getStringExtra(AddEditLube.COST_TAG));
//        costTextView.setText(newCost);
        sellTextView.setText(getIntent().getStringExtra(AddEditLube.SELL_TAG) + " ---> (" + newCost + ')');
        noteTextView.setText(getIntent().getStringExtra(AddEditLube.NOTE_TAG));



        // Set up local variables with data by passing intent
        if(getIntent().hasExtra(ID_TAG)) {
            id = getIntent().getIntExtra(ID_TAG, 0);
        }
        brand = getIntent().getStringExtra(AddEditLube.BRAND_TAG);
        model = getIntent().getStringExtra(AddEditLube.MODEL_TAG);
        grade = getIntent().getStringExtra(AddEditLube.GRADE_TAG);
        viscosity = getIntent().getStringExtra(AddEditLube.VISCOSITY_TAG);
        location = getIntent().getStringExtra(AddEditLube.LOCATION_TAG);
        partNumberOE = getIntent().getStringExtra(AddEditLube.PART_NUMBER_OE_TAG);
        content = getIntent().getStringExtra(AddEditLube.CONTENT_TAG);
        cost = getIntent().getStringExtra(AddEditLube.COST_TAG);
        profit = getIntent().getStringExtra(AddEditLube.PROFIT_TAG);
        sell = getIntent().getStringExtra(AddEditLube.SELL_TAG);
        note = getIntent().getStringExtra(AddEditLube.NOTE_TAG);
        time = getIntent().getStringExtra(TIME_TAG);
        if(getIntent().hasExtra(IMAGE_TAG) && getIntent().getByteArrayExtra(IMAGE_TAG) != null) {
            image = getIntent().getByteArrayExtra(IMAGE_TAG);
            ByteArrayInputStream bis = new ByteArrayInputStream(image);
            Bitmap photo = BitmapFactory.decodeStream(bis);
            lubeImageView.setImageBitmap(photo);
        }



        // Creating variable for BiometricManager
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            // This means biometric sensor is enabled
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(ViewLube.this, "Login via fingerprint or key in password", Toast.LENGTH_SHORT).show();
                break;
            // This means there is no biometric support for the device
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(ViewLube.this, "Biometric not supported on your device", Toast.LENGTH_SHORT).show();
                break;
            // This means biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(ViewLube.this, "Biometric currently not available", Toast.LENGTH_LONG).show();
                break;
            // This means biometric sensor does not contain any fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(ViewLube.this, "No saved fingerprint found", Toast.LENGTH_LONG).show();
                break;
        }


        // Creating variable for executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // Result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(ViewLube.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // When AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Gather data to be pass for editing
                Toast.makeText(ViewLube.this, "Login successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ViewLube.this, AddEditLube.class);
                intent.putExtra(ID_TAG, id);
                intent.putExtra(BRAND_TAG, brand);
                intent.putExtra(MODEL_TAG, model);
                intent.putExtra(GRADE_TAG, grade);
                intent.putExtra(VISCOSITY_TAG, viscosity);
                intent.putExtra(LOCATION_TAG, location);
                intent.putExtra(PART_NUMBER_OE_TAG, partNumberOE);
                intent.putExtra(CONTENT_TAG, content);
                intent.putExtra(COST_TAG, cost);
                intent.putExtra(PROFIT_TAG, profit);
                intent.putExtra(SELL_TAG, sell);
                intent.putExtra(NOTE_TAG, note);
                intent.putExtra(TIME_TAG, time);
                if(image != null) {
                    intent.putExtra(IMAGE_TAG, image);
                } else {
                    intent.putExtra(IMAGE_TAG, (byte[]) null);
                }
                mStartForResult.launch(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });


        // Creating final variable for prompt
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Hardware Found")
                .setDescription("Use your fingerprint to login").setNegativeButtonText("Cancel").build();

        // Biometric prompt dialog
//        biometricPrompt.authenticate(promptInfo);









        // Set onCLickListener for edit button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });









        // Creating variable for executor
        Executor executor2 = ContextCompat.getMainExecutor(this);
        // Result of AUTHENTICATION
        final BiometricPrompt biometricPrompt2 = new BiometricPrompt(ViewLube.this, executor2, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // When AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Delete entry from database with biometric confirmation
                LubeEntity lubeEntity = new LubeEntity(brand, model, grade, viscosity, content, location,
                        cost, profit, sell, note, image, time);
                lubeEntity.setId(id);
                LubeViewModel.deleteLube(lubeEntity);
                Toast.makeText(ViewLube.this, "Lube successfully deleted", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });





        // Set onClickListener for delete button
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt2.authenticate(promptInfo);
            }
        });





        // Handle image enlargement and zooming when user clicked on the image
        if(getIntent().getByteArrayExtra(IMAGE_TAG) != null) {
            lubeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(ViewLube.this);
                    dialog.setContentView(R.layout.simple_camera);

                    ImageView imageEnlarge = dialog.findViewById(R.id.enlarge_image);

                    // Get the applied image from imageView as a bitmap
                    Bitmap image = ((BitmapDrawable) lubeImageView.getDrawable()).getBitmap();

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




        // Translate location to other languages
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
                    // Check the code for the language you want to use under TranslateApi.java class
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
                    // Check for the code of your language in use and to translate to in TranslateApi.java class
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