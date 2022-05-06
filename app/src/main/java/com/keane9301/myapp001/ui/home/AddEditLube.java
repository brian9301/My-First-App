package com.keane9301.myapp001.ui.home;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.keane9301.myapp001.Database.Utils.Conversion;
import com.keane9301.myapp001.Database.Utils.DefaultForEmptyField;
import com.keane9301.myapp001.Database.Utils.ResizeBitmap;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.databinding.ActivityAddEditLubeBinding;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddEditLube extends AppCompatActivity {
    private ActivityAddEditLubeBinding lubeBinding;
    private int PERMISSION_ID = 80;
    public static String TAG = "DEBUG_TAG";

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

    private TextView addEditTitle;
    private TextView addEditHint;

    private EditText brandEditText;
    private EditText modelEditText;
    private EditText gradeEditText;
    private EditText viscosityEditText;
    private EditText locationEditText;
    private EditText partNumberOEEditText;
    private EditText contentEditText;
    private EditText costEditText;
    private EditText profitEditText;
    private EditText sellEditText;
    private EditText noteEditText;
    private ImageView lubeImageView;
    private ImageButton cameraButton;
    private ImageButton galleryButton;
    private Button saveButton;
    private Button cancelButton;

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
    String currentPhotoPath;









    // Setting imageView with photo taken via camera
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        File file = new File(currentPhotoPath);
                        Uri uri = Uri.fromFile(file);
                        Bitmap bitmap;
                        // Convert image to byteArray and resize
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            byte[] newArray = ResizeBitmap.downsizeBitmapImageArray(bitmap);
                            ByteArrayInputStream bis = new ByteArrayInputStream(newArray);
                            Bitmap newBitmap = BitmapFactory.decodeStream(bis);
                            lubeImageView.setImageBitmap(newBitmap);
                            image = newArray;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });




    // Setting imageView with photo selected from gallery
    ActivityResultLauncher<Intent> galleryStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        assert intent != null;
                        Uri target = intent.getData();
                        // Get image and convert the image to byteArray and downsize
                        try {
                            Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(target));
                            byte[] newArray = ResizeBitmap.downsizeBitmapImageArray(photo);
                            ByteArrayInputStream bis = new ByteArrayInputStream(newArray);
                            Bitmap newBitmap = BitmapFactory.decodeStream(bis);
                            lubeImageView.setImageBitmap(newBitmap);
                            image = newArray;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_edit_lube);

        // Disable screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Wire up all buttons and edit texts
        lubeBinding = DataBindingUtil.setContentView(AddEditLube.this, R.layout.activity_add_edit_lube);

        brandEditText = lubeBinding.brandTextEditView;
        modelEditText = lubeBinding.modelTextEditView;
        gradeEditText = lubeBinding.gradeTextEditView;
        viscosityEditText = lubeBinding.viscosityTextEditView;
        locationEditText = lubeBinding.locationTextEditView;
        partNumberOEEditText = lubeBinding.oePartNumberTextEditView;
        contentEditText = lubeBinding.contentTextEditView;
        costEditText = lubeBinding.costTextEditView;
        profitEditText = lubeBinding.profitTextEditView;
        sellEditText = lubeBinding.sellTextEditView;
        noteEditText = lubeBinding.noteTextEditView;

        lubeImageView = lubeBinding.lubeAddEditImageView;
        cameraButton = lubeBinding.cameraImageButton;
        galleryButton = lubeBinding.galleryImageButton;
        saveButton = lubeBinding.saveButton;
        cancelButton = lubeBinding.cancelButton;

        addEditTitle = lubeBinding.addEditTitle;
        addEditHint = lubeBinding.addEditHint;




        // Change the title when intent contains data
        if(getIntent().getStringExtra(ViewLube.BRAND_TAG) != null &&
                !getIntent().getStringExtra(ViewLube.BRAND_TAG).equals("N/A")) {
            addEditTitle.setText(R.string.edit_form);
            addEditHint.setText(R.string.edit_hint);
        }



        // Remove current image when user clicked on imageView
        if(lubeImageView != null) {
            lubeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    lubeImageView.setImageResource(android.R.color.transparent);
//                    lubeImageView.setImageResource(R.drawable.ic_menu_gallery);
                    lubeImageView.setImageDrawable(null);
                    lubeImageView.destroyDrawingCache();
                    image = null;
                }
            });
        }



        // Setting variables with their appropriate intent data
        id = getIntent().getIntExtra(ViewLube.ID_TAG, 0);
        brandEditText.setText(getIntent().getStringExtra(ViewLube.BRAND_TAG));
        modelEditText.setText(getIntent().getStringExtra(ViewLube.MODEL_TAG));
        gradeEditText.setText(getIntent().getStringExtra(ViewLube.GRADE_TAG));
        viscosityEditText.setText(getIntent().getStringExtra(ViewLube.VISCOSITY_TAG));
        locationEditText.setText(getIntent().getStringExtra(ViewLube.LOCATION_TAG));
        partNumberOEEditText.setText(getIntent().getStringExtra(ViewLube.PART_NUMBER_OE_TAG));
        contentEditText.setText(getIntent().getStringExtra(ViewLube.CONTENT_TAG));
        costEditText.setText(getIntent().getStringExtra(ViewLube.COST_TAG));
        profitEditText.setText(getIntent().getStringExtra(ViewLube.PROFIT_TAG));
        sellEditText.setText(getIntent().getStringExtra(ViewLube.SELL_TAG));
        noteEditText.setText(getIntent().getStringExtra(ViewLube.NOTE_TAG));
        if(getIntent().getByteArrayExtra(ViewLube.IMAGE_TAG) != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(getIntent().getByteArrayExtra(ViewLube.IMAGE_TAG));
            Bitmap photo = BitmapFactory.decodeStream(bis);
            lubeImageView.setImageBitmap(photo);
        }





        // Setting onClickListener for camera and gallery image buttons and cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                    Log.d(TAG, "onClick: launching intent");
                if(checkPermissionAccess()) {
                    dispatchTakePhotoIntent();
                    Log.d(TAG, "onClick: launching intent 2");
                } else {
                    requestForPermissions();
                }
            }
        });


        galleryButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                if(checkPermissionAccess()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryStartForResult.launch(intent);
                } else {
                    requestForPermissions();
                }
            }
        });









        // Set save button listener and passing data collected to be save in database
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting data from user input
                brand = brandEditText.getText().toString().trim();
                model = modelEditText.getText().toString().trim();
                grade = gradeEditText.getText().toString().trim();
                viscosity = viscosityEditText.getText().toString().trim();
                location = locationEditText.getText().toString().trim();
                partNumberOE = partNumberOEEditText.getText().toString().trim();
                content = contentEditText.getText().toString().trim();
                cost = costEditText.getText().toString().trim();
                profit = profitEditText.getText().toString().trim();
                sell = sellEditText.getText().toString().trim();
                note = noteEditText.getText().toString().trim();

                // Getting date and time when save button is clicked
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy   HH:mm:ss");
                time = simpleDateFormat.format(calendar.getTime());

                // Set default if field is left empty by user
                ArrayList<String> defaultData = DefaultForEmptyField.insertDefaultForLube(brand, model,
                        grade, viscosity, location, partNumberOE, content, cost, profit, sell, note);

                // Replace data with user input and default
                brand = defaultData.get(0);
                model = defaultData.get(1);
                grade = defaultData.get(2);
                viscosity = defaultData.get(3);
                location = defaultData.get(4);
                partNumberOE = defaultData.get(5);
                content = defaultData.get(6);
                cost = defaultData.get(7);
                profit = defaultData.get(8);
                sell = defaultData.get(9);
                note = defaultData.get(10);

                Log.d(TAG, "onClick: " + brand + ' ' + model + ' ' + cost);

                // Passing intent back for processing either store as new entry or update existing
                Intent intent = new Intent();
                if(getIntent().hasExtra(ViewLube.ID_TAG)) {
                    intent.putExtra(ViewLube.ID_TAG, id);
                }
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
                if(image != null) {
                    intent.putExtra(IMAGE_TAG, image);
                Log.d(TAG, "onClick: image array = " + Arrays.toString(image));
                } else {
                    intent.putExtra(IMAGE_TAG, (byte[]) null);
                }
                intent.putExtra(TIME_TAG, time);

                setResult(RESULT_OK, intent);
                finish();
            }
        });







    }




    // Check for camera and allow use it to take photo
    Uri photoUri;
    private void dispatchTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure there is a camera action to handle this event
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create file to store photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d(TAG, "dispatchTakePhotoIntent: success in try");
            } catch (IOException e) {
                // Error occurred creating the file
                e.printStackTrace();
            }

            // Continue if file is successfully created
            // Authority need to setup in Manifest and create xml folder in res
            // Create file_paths.xml in xml folder with codes from Google Dev web
            if(photoFile != null) {
                Log.d(TAG, "dispatchTakePhotoIntent: in if");
                photoUri = FileProvider.getUriForFile(this, "com.keane9301.myapp001.fileprovider", photoFile);
                Log.d(TAG, "dispatchTakePhotoIntent: launching");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                mStartForResult.launch(takePictureIntent);
            }
        }
    }



    // Create a path to store camera taken photo
    String timestamp;
    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {
        // Create image file name
        timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file path for use with ACTION VIEW INTENT
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }




    // Checking for permission granted
    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean checkPermissionAccess() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }



    // Asking for permission from user
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestForPermissions() {
        ActivityCompat.requestPermissions(AddEditLube.this, new String[] {
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSION_ID);
    }











}