package com.keane9301.myapp001.ui.gallery;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
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

import com.keane9301.myapp001.Database.Parts.PartEntity;
import com.keane9301.myapp001.Database.Parts.PartViewModel;
import com.keane9301.myapp001.Database.Utils.Conversion;
import com.keane9301.myapp001.Database.Utils.DefaultForEmptyField;
import com.keane9301.myapp001.Database.Utils.ResizeBitmap;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.databinding.ActivityAddEditPartBinding;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddEditPart extends AppCompatActivity {
    public static String TAG = "DEBUG_TAG";
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


    private ActivityAddEditPartBinding partBinding;
    private int CAMERA_REQUEST_CODE = 55;
    private int PERMISSION_ID = 88;
    private int GALLERY_CODE = 20;

    private TextView addEditTitle;
    private TextView addEditHint;

    private EditText applicationEditText;
    private EditText categoryEditText;
    private EditText subCategoryEditText;
    private EditText positionEditText;
    private EditText brandEditText;
    private EditText conditionEditText;
    private EditText sizeEditField;
    private EditText partNumberEditText;
    private EditText partNumberAltEditText;
    private EditText partNumberOEEditText;
    private EditText partWarrantyEditText;
    private EditText locationEditText;
    private EditText supplierEditText;
    private EditText costEditText;
    private EditText profitEditText;
    private EditText sellEditText;
    private EditText noteEditText;

    private ImageButton galleryButton;
    private ImageButton cameraButton;
    private ImageView imageView;
    private Button saveButton;
    private Button cancelButton;

    private int id;
    private String application;
    private String category;
    private String subCategory;
    private String position;
    private String brand;
    private String condition;
    private String size;
    private String partNumber;
    private String partNumberAlt;
    private String partNumberOE;
    private String partWarranty;
    private String location;
    private String supplier;
    private String cost;
    private String profit;
    private String sell;
    private String note;
    private byte[] image;
    private String time;

    private byte[] byteImage;




    // Verify data for camera capture
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        File file = new File(currentPhotoPath);
                        Uri uri = Uri.fromFile(file);
                        Bitmap bitmap;
                        // Convert image to byteArray and resize
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            byte[] newArray = ResizeBitmap.downsizeBitmapImageArray(bitmap);
                            ByteArrayInputStream bis = new ByteArrayInputStream(newArray);
                            Bitmap newBitmap = BitmapFactory.decodeStream(bis);
                            imageView.setImageBitmap(newBitmap);
                            byteImage = newArray;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });



    // Verify data for selecting image from gallery
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        Uri target = data.getData();
                        // Get image and convert the image to byteArray and downsize
                        try {
                            Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(target));
                            byte[] newArray = ResizeBitmap.downsizeBitmapImageArray(photo);
                            ByteArrayInputStream bis = new ByteArrayInputStream(newArray);
                            Bitmap newPhoto = BitmapFactory.decodeStream(bis);
                            imageView.setImageBitmap(newPhoto);
                            byteImage = newArray;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_edit_part);

        // Disable screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        // Wire up all buttons and edit texts
        partBinding = DataBindingUtil.setContentView(AddEditPart.this, R.layout.activity_add_edit_part);

        applicationEditText = partBinding.applicationTextEditField;
        categoryEditText = partBinding.categoryTextEditField;
        subCategoryEditText = partBinding.subCategoryTextEditField;
        positionEditText = partBinding.positionTextEditField;
        brandEditText = partBinding.brandPartTextEditField;
        conditionEditText = partBinding.conditionPartTextEditField;
        sizeEditField = partBinding.sizePartTextEditField;
        partNumberEditText = partBinding.partNumberPartTextEditField;
        partNumberAltEditText = partBinding.partNumberAltPartTextEditField;
        partNumberOEEditText = partBinding.oePartNumberPartTextEditField;
        partWarrantyEditText = partBinding.partWarrantyPartTextEditField;
        locationEditText = partBinding.locationPartTextEditField;
        supplierEditText = partBinding.supplierPartTextEditField;
        costEditText = partBinding.costPartTextEditField;
        profitEditText = partBinding.profitPartTextEditField;
        sellEditText = partBinding.sellPartTextEditField;
        noteEditText = partBinding.notePartTextEditField;

        imageView = partBinding.imageViewPart;
        galleryButton = partBinding.galleryPartImageButton;
        cameraButton = partBinding.cameraPartImageButton;
        saveButton = partBinding.savePartButton;
        cancelButton = partBinding.cancelPartButton;

        addEditTitle = partBinding.addEditPartTitle;
        addEditHint = partBinding.addEditPartHint;




        // Change the title when an intent is available
        if(getIntent().getStringExtra(ViewPart.APPLICATION_TAG) != null &&
            !getIntent().getStringExtra(ViewPart.APPLICATION_TAG).equals("N/A")) {
            addEditTitle.setText(R.string.edit_form);
            addEditHint.setText(R.string.edit_hint);
        }



        // Setting each variable with the appropriate data
        id = getIntent().getIntExtra(ViewPart.ID_TAG, 0);
        application = getIntent().getStringExtra(ViewPart.APPLICATION_TAG);
        category = getIntent().getStringExtra(ViewPart.CATEGORY_TAG);
        subCategory = getIntent().getStringExtra(ViewPart.SUBCATEGORY_TAG);
        position = getIntent().getStringExtra(ViewPart.POSITION_TAG);
        supplier = getIntent().getStringExtra(ViewPart.SUPPLIER_TAG);
        brand = getIntent().getStringExtra(ViewPart.BRAND_TAG);
        condition = getIntent().getStringExtra(ViewPart.CONDITION_TAG);
        size = getIntent().getStringExtra(ViewPart.SIZE_TAG);
        partNumber = getIntent().getStringExtra(ViewPart.PART_NUMBER_TAG);
        partNumberAlt = getIntent().getStringExtra(ViewPart.PART_NUMBER_ALT_TAG);
        partNumberOE = getIntent().getStringExtra(ViewPart.PART_NUMBER_OE_TAG);
        partWarranty = getIntent().getStringExtra(ViewPart.PART_WARRANTY_TAG);
        location = getIntent().getStringExtra(ViewPart.LOCATION_TAG);
        cost = getIntent().getStringExtra(ViewPart.COST_TAG);
        profit = getIntent().getStringExtra(ViewPart.PROFIT_TAG);
        sell = getIntent().getStringExtra(ViewPart.SELL_TAG);
        note = getIntent().getStringExtra(ViewPart.NOTE_TAG);
        byteImage = getIntent().getByteArrayExtra(ViewPart.IMAGE_TAG);
        time = getIntent().getStringExtra(ViewPart.TIME_TAG);
        Log.d(TAG, "onCreate: " + time);



        // Remove current image when user clicked on the imageView
        if(imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageDrawable(null);
                    imageView.destroyDrawingCache();
                    byteImage = null;
                }
            });
        }


        // Filling up the forms with data when intent was received
        applicationEditText.setText(application);
        categoryEditText.setText(category);
        subCategoryEditText.setText(subCategory);
        positionEditText.setText(position);
        brandEditText.setText(brand);
        conditionEditText.setText(condition);
        sizeEditField.setText(size);
        partNumberEditText.setText(partNumber);
        partNumberAltEditText.setText(partNumberAlt);
        partNumberOEEditText.setText(partNumberOE);
        partWarrantyEditText.setText(partWarranty);
        locationEditText.setText(location);
        supplierEditText.setText(supplier);
        costEditText.setText(cost);
        profitEditText.setText(profit);
        sellEditText.setText(sell);
        noteEditText.setText(note);
        if(byteImage != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteImage);
            Bitmap photoView = BitmapFactory.decodeStream(bis);
            imageView.setImageBitmap(photoView);
        }





        // Setting onClickListener for camera and gallery image buttons and cancel button
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionAccess()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, GALLERY_CODE);
                    galleryActivityResultLauncher.launch(intent);
                } else {
                    requestForPermissions();
                }
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermissionAccess()) {
                    dispatchTakePhotoIntent();
                    Log.d(TAG, "onClick: launch intent");
                } else {
                    requestForPermissions();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        // Set save button listener and passing data collected to be save in database
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting data from user input or after user has done editing
                application = applicationEditText.getText().toString().trim();
                category = categoryEditText.getText().toString().trim();
                subCategory = subCategoryEditText.getText().toString().trim();
                position = positionEditText.getText().toString().trim();
                brand = brandEditText.getText().toString().trim();
                condition = conditionEditText.getText().toString().trim();
                size = sizeEditField.getText().toString().trim();
                partNumber = partNumberEditText.getText().toString().trim();
                partNumberAlt = partNumberAltEditText.getText().toString().trim();
                partNumberOE = partNumberOEEditText.getText().toString().trim();
                partWarranty = partWarrantyEditText.getText().toString().trim();
                location = locationEditText.getText().toString().trim();
                supplier = supplierEditText.getText().toString().trim();
                cost = costEditText.getText().toString().trim();
                profit = profitEditText.getText().toString().trim();
                sell = sellEditText.getText().toString().trim();
                note = noteEditText.getText().toString().trim();



                // Get time and date when button is clicked
                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy   HH:mm:ss");
                time = simpleDateFormat.format(calendar.getTime());

                // Set empty field with default when user left it unfilled
                ArrayList<String> defaultSet = DefaultForEmptyField.insertDefaultForPart(application,
                        category, subCategory, position, brand, condition, size, partNumber, partNumberAlt,
                        partNumberOE, partWarranty, location, supplier, cost, profit, sell, note);

                // Setting up so every field is filled with either user data or default
                application = defaultSet.get(0);
                category = defaultSet.get(1);
                subCategory = defaultSet.get(2);
                position = defaultSet.get(3);
                brand = defaultSet.get(4);
                condition = defaultSet.get(5);
                size = defaultSet.get(6);
                partNumber = defaultSet.get(7);
                partNumberAlt = defaultSet.get(8);
                partNumberOE = defaultSet.get(9);
                partWarranty = defaultSet.get(10);
                location = defaultSet.get(11);
                supplier = defaultSet.get(12);
                cost = defaultSet.get(13);
                profit = defaultSet.get(14);
                sell = defaultSet.get(15);
                note = defaultSet.get(16);

                Log.d(TAG, "onClick: " + id + ' ' + application + ' ' + category + ' ' + profit);


                // Passing intent back for processing either to store as new or to update
                Intent intent = new Intent();
                if(id != 0) {
                    intent.putExtra(ViewPart.ID_TAG, id);
                }
                intent.putExtra(APPLICATION_TAG, application);
                intent.putExtra(CATEGORY_TAG, category);
                intent.putExtra(SUBCATEGORY_TAG, subCategory);
                intent.putExtra(POSITION_TAG, position);
                intent.putExtra(BRAND_TAG, brand);
                intent.putExtra(CONDITION_TAG, condition);
                intent.putExtra(SIZE_TAG, size);
                intent.putExtra(PART_NUMBER_TAG, partNumber);
                intent.putExtra(PART_NUMBER_ALT_TAG, partNumberAlt);
                intent.putExtra(PART_NUMBER_OE_TAG, partNumberOE);
                intent.putExtra(PART_WARRANTY_TAG, partWarranty);
                intent.putExtra(LOCATION_TAG, location);
                intent.putExtra(SUPPLIER_TAG, supplier);
                intent.putExtra(COST_TAG, cost);
                intent.putExtra(PROFIT_TAG, profit);
                intent.putExtra(SELL_TAG, sell);
                intent.putExtra(NOTE_TAG, note);
                if(byteImage != null) {
                    intent.putExtra(IMAGE_TAG, byteImage);
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
//                photoUri = FileProvider.getUriForFile(this, "${applicationId}.provider", photoFile);
                photoUri = FileProvider.getUriForFile(this, "com.keane9301.myapp001.fileprovider", photoFile);
                Log.d(TAG, "dispatchTakePhotoIntent: launching");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                cameraActivityResultLauncher.launch(takePictureIntent);
            }
        }
    }



    // Create a path to store camera taken photo
    String currentPhotoPath;
    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {
        // Create image file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file path for use with ACTION VIEW INTENT
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    // Checking for permission granted
    private boolean checkPermissionAccess() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }



    // Request user for permission if not granted yet
    private void requestForPermissions() {
        ActivityCompat.requestPermissions(AddEditPart.this, new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSION_ID);
    }




    // This has been replaced by ActivityResultLauncher method and no longer in use
    // Verify intent result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Setting imageView with photo taken via camera
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);
            Uri uri = Uri.fromFile(file);
            Bitmap bitmap;
            // Convert image to byteArray and resize
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                byte[] newArray = ResizeBitmap.downsizeBitmapImageArray(bitmap);
                ByteArrayInputStream bis = new ByteArrayInputStream(newArray);
                Bitmap newBitmap = BitmapFactory.decodeStream(bis);

                imageView.setImageBitmap(newBitmap);
                byteImage = newArray;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Setting imageView with photo selected from gallery
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            assert data != null;
            Uri target = data.getData();
            // Get image and convert the image to byteArray and downsize
            try {
                Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(target));
                byte[] newArray = ResizeBitmap.downsizeBitmapImageArray(photo);
                ByteArrayInputStream bis = new ByteArrayInputStream(newArray);
                Bitmap newPhoto = BitmapFactory.decodeStream(bis);

                imageView.setImageBitmap(newPhoto);
                byteImage = newArray;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }




    // Method use to rotate current image? How does this work?
    public int getCameraPhotoOrientation(Context context, Uri imageUri,
                                         String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }










}