package com.keane9301.myapp001.ui.slideshow;

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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.keane9301.myapp001.Database.Utils.DefaultForEmptyField;
import com.keane9301.myapp001.Database.Utils.ResizeBitmap;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.databinding.ActivityAddEditCustomerBinding;
import com.keane9301.myapp001.ui.home.AddEditLube;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AddEditCustomer extends AppCompatActivity {
    private static final int LOC_ID = 25;
    private ActivityAddEditCustomerBinding customerBinding;

    public static String TAG = "DEBUG_TAG";
    public static String ID_TAG = "idTag";
    public static String NAME_TAG = "nameTag";
    public static String IDENTIFICATION_TAG = "identificationTag";
    public static String SHOP_TAG = "shopTag";
    public static String REGISTRATION_TAG = "registrationTag";
    public static String PHONE_TAG = "phoneTag";
    public static String PHONE_ALT_TAG = "phoneAltTag";
    public static String ADDRESS_TAG = "addressTag";
    public static String LOC_LAT_LNG_TAG = "locLatLngTag";
    public static String TIME_TAG = "timeTag";
    public static String IMAGE_TAG = "imageTag";

    private TextView addEditTitle;
    private TextView addEditHint;

    private EditText nameEditText;
    private EditText identificationEditText;
    private EditText shopEditText;
    private EditText registrationEditText;
    private EditText phoneEditText;
    private EditText phoneAltEditText;
    private EditText addressEditText;
    private EditText locLatLngEditText;
    private ImageButton locationImageButton;
    private ImageButton cameraImageButton;
    private ImageButton galleryImageButton;
    private ImageView customerImage;
    private Button saveButton;
    private Button cancelButton;

    private final int PERMISSION_ID = 30;

    private int id;
    private String name;
    private String identification;
    private String shop;
    private String registration;
    private String phone;
    private String phoneAlt;
    private String address;
    private String locLatLng;
    private String time;
    private byte[] image;
    private String currentPhotoPath;

    private String  USER_LOCATION_LATITUDE;
    private String  USER_LOCATION_LONGITUDE;







    // Getting the location info via Map and GPS (latitude and longitude)
    ActivityResultLauncher<Intent> mStartForMapResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onActivityResult(ActivityResult result) {
//                    Log.d(TAG, "onActivityResult: lat lng " + result.getResultCode());
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        assert intent != null;
//                        Log.d(TAG, "onActivityResult: lat lng " + result.getResultCode());
                        USER_LOCATION_LATITUDE = (intent.getStringExtra(MapsActivity.USER_LAT));
                        USER_LOCATION_LONGITUDE = (intent.getStringExtra(MapsActivity.USER_LNG));
                        locLatLngEditText.setText(USER_LOCATION_LATITUDE + ", " + USER_LOCATION_LONGITUDE);
                    }
                }
            });




    // Getting image from camera capture
    ActivityResultLauncher<Intent> mStartForResultCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
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
                            customerImage.setImageBitmap(newBitmap);
                            image = newArray;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });



    // Getting image input from gallery selected by user
    ActivityResultLauncher<Intent> mStartForResultGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
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
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(target));
                            byte[] newArray = ResizeBitmap.downsizeBitmapImageArray(bitmap);
                            ByteArrayInputStream bis = new ByteArrayInputStream(newArray);
                            Bitmap photo = BitmapFactory.decodeStream(bis);
                            customerImage.setImageBitmap(photo);
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
//        setContentView(R.layout.activity_add_edit_customer);

        // Block screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        // Setup view
        customerBinding = DataBindingUtil.setContentView(AddEditCustomer.this, R.layout.activity_add_edit_customer);

        // Wire up edit texts and buttons
        nameEditText = customerBinding.nameCusTextEdit;
        identificationEditText = customerBinding.identificationCusTextEdit;
        shopEditText = customerBinding.shopCusTextEdit;
        registrationEditText = customerBinding.registrationCusTextEdit;
        phoneEditText = customerBinding.numberCusTextEdit;
        phoneAltEditText = customerBinding.numberAltCusTextEdit;
        addressEditText = customerBinding.addressCusTextEdit;
        locLatLngEditText = customerBinding.locationCusTextEdit;

        locationImageButton = customerBinding.locationImageButton;
        cameraImageButton = customerBinding.cameraImageButton;
        galleryImageButton = customerBinding.galleryImageButton;
        customerImage = customerBinding.customerAddEditImageView;
        saveButton = customerBinding.saveButton;
        cancelButton = customerBinding.cancelButton;

        addEditTitle = customerBinding.addEditCustomerTitle;
        addEditHint = customerBinding.addEditCustomerHint;







        // Setting title and hint either add or edit based on intent
        if(getIntent().getStringExtra(ViewCustomer.NAME_TAG) != null &&
                !getIntent().getStringExtra(ViewCustomer.NAME_TAG).equals("N/A")) {
            addEditTitle.setText(R.string.edit_form);
            addEditHint.setText(R.string.edit_hint);
        }


        // Set texts when intent is available
        nameEditText.setText(getIntent().getStringExtra(ViewCustomer.NAME_TAG));
        identificationEditText.setText(getIntent().getStringExtra(ViewCustomer.IDENTIFICATION_TAG));
        shopEditText.setText(getIntent().getStringExtra(ViewCustomer.SHOP_TAG));
        registrationEditText.setText(getIntent().getStringExtra(ViewCustomer.REGISTRATION_TAG));
        phoneEditText.setText(getIntent().getStringExtra(ViewCustomer.PHONE_TAG));
        phoneAltEditText.setText(getIntent().getStringExtra(ViewCustomer.PHONE_ALT_TAG));
        addressEditText.setText(getIntent().getStringExtra(ViewCustomer.ADDRESS_TAG));
        locLatLngEditText.setText(getIntent().getStringExtra(ViewCustomer.LOC_LAT_LNG_TAG));
        if(getIntent().getByteArrayExtra(ViewCustomer.IMAGE_TAG) != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(getIntent().getByteArrayExtra(ViewCustomer.IMAGE_TAG));
            Bitmap photo = BitmapFactory.decodeStream(bis);
            customerImage.setImageBitmap(photo);
        }




        if(customerImage != null) {
            customerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerImage.setImageDrawable(null);
                    customerImage.destroyDrawingCache();
                    image = null;
                }
            });
        }



        // Set button on click listener
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Launch map service when location button clicked, permission is requested in Map activity
        locationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEditCustomer.this, MapsActivity.class);
                mStartForMapResult.launch(intent);
            }
        });



        // Saving either new or edited of the current customer
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting all the inputs
                if(getIntent().hasExtra(ViewCustomer.ID_TAG)) {
                    id = getIntent().getIntExtra(ViewCustomer.ID_TAG, 0);
                }
                name = nameEditText.getText().toString().trim();
                identification = identificationEditText.getText().toString().trim();
                shop = shopEditText.getText().toString().trim();
                registration = registrationEditText.getText().toString().trim();
                phone = phoneEditText.getText().toString().trim();
                phoneAlt = phoneAltEditText.getText().toString().trim();
                address = addressEditText.getText().toString().trim();
                locLatLng = locLatLngEditText.getText().toString().trim();

                // Setting the date and time it was added or edited
                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy   HH:mm:ss");
                time = simpleDateFormat.format(calendar.getTime());

                // Set a default when user leave a blank
                ArrayList<String> defaultData = DefaultForEmptyField.insertDefaultForCustomer(name,
                        identification ,shop, registration, phone, phoneAlt, address, locLatLng);

                name = defaultData.get(0);
                identification = defaultData.get(1);
                shop = defaultData.get(2);
                registration = defaultData.get(3);
                phone = defaultData.get(4);
                phoneAlt = defaultData.get(5);
                address = defaultData.get(6);
                locLatLng = defaultData.get(7);


                // Pass the data back for processing either to create new entry or update existing
                Intent intent = new Intent();
                if(getIntent().getStringExtra(ViewCustomer.ID_TAG) != null) {
                    intent.putExtra(ID_TAG, getIntent().getIntExtra(ViewCustomer.ID_TAG, 0));
                    Log.d(TAG, "onClick: " + getIntent().getIntExtra(ViewCustomer.ID_TAG, 0));
                }
                intent.putExtra(NAME_TAG, name);
                intent.putExtra(IDENTIFICATION_TAG, identification);
                intent.putExtra(SHOP_TAG, shop);
                intent.putExtra(REGISTRATION_TAG, registration);
                intent.putExtra(PHONE_TAG, phone);
                intent.putExtra(PHONE_ALT_TAG, phoneAlt);
                intent.putExtra(ADDRESS_TAG, address);
                intent.putExtra(LOC_LAT_LNG_TAG, locLatLng);
                if(image != null) {
                    intent.putExtra(IMAGE_TAG, image);
                } else {
                    intent.putExtra(IMAGE_TAG, (byte[]) null);
                }
                intent.putExtra(TIME_TAG, time);
                setResult(RESULT_OK, intent);
                Log.d(TAG, "onClick: intent passed " + phone);
                finish();
            }
        });




        // Camera image button listener, launch camera when clicked but only when permission to use camera is given
        cameraImageButton.setOnClickListener(new View.OnClickListener() {
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


        // Launch gallery when clicked if permission to access storage is given
        galleryImageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                if(checkPermissionAccess()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    mStartForResultGallery.launch(intent);
                } else {
                    requestForPermissions();
                }
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
                mStartForResultCamera.launch(takePictureIntent);
            }
        }
    }



    // Create a path to store camera taken photo
    private File createImageFile() throws IOException {
        // Create image file name
        @SuppressLint("SimpleDateFormat") String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
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



    // Checking for location access permission from user (Moved to be checked in MapsActivity class)
    private boolean checkLocationAccess() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }



    // Asking for permission from user
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestForPermissions() {
        ActivityCompat.requestPermissions(AddEditCustomer.this, new String[] {
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
//                , Manifest.permission.MANAGE_EXTERNAL_STORAGE
        }, PERMISSION_ID);
    }


    // Getting location permission when button is clicked if user have not given yet
    // Currently being check under MapsActivity class
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(AddEditCustomer.this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        }, LOC_ID);
    }






}
