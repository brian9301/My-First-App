package com.keane9301.myapp001.ui.slideshow;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.keane9301.myapp001.Database.Customers.CustomerEntity;
import com.keane9301.myapp001.Database.Customers.CustomerViewModel;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.ViewsAndAdapters.CustomerRecyclerViewAdapter;
import com.keane9301.myapp001.ui.home.AddEditLube;
import com.keane9301.myapp001.ui.home.ViewLube;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewCustomer extends AppCompatActivity {

    private final int PERMISSION_ID = 10;
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

    private TextView nameTextView;
    private TextView identificationTextView;
    private TextView shopTextView;
    private TextView registrationTextView;
    private TextView phoneTextView;
    private TextView phoneAltTextView;
    private TextView addressTextView;
    private TextView locLatLngTextView;
    private ImageButton copyLatLngImageButton;
    private ImageButton copyNumberImageButton;
    private ImageButton copyNumberAltImageButton;
    private ImageButton cameraImageButton;
    private ImageButton galleryImageButton;
    private ImageView customerImage;
    private Button listButton;
    private Button editButton;
    private Button deleteButton;
    private TextView modifiedDateTime;

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







    // Receive result after editing has been done
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        assert intent != null;
                        name = intent.getStringExtra(NAME_TAG);
                        identification = intent.getStringExtra(IDENTIFICATION_TAG);
                        shop = intent.getStringExtra(SHOP_TAG);
                        registration = intent.getStringExtra(REGISTRATION_TAG);
                        address = intent.getStringExtra(ADDRESS_TAG);
                        locLatLng = intent.getStringExtra(LOC_LAT_LNG_TAG);
                        phone = intent.getStringExtra(PHONE_TAG);
                        phoneAlt = intent.getStringExtra(PHONE_ALT_TAG);
                        time = intent.getStringExtra(TIME_TAG);
                        image = intent.getByteArrayExtra(IMAGE_TAG);

                        Log.d(TAG, "onActivityResult: " + phone + ' ' + id + ' ' + name +
                                "\n" + intent.getStringExtra(PHONE_TAG));
                        CustomerEntity customerEntity = new CustomerEntity(name, identification, shop,
                                registration, intent.getStringExtra(PHONE_TAG),
                                intent.getStringExtra(PHONE_ALT_TAG), address, locLatLng, time, image);
                        customerEntity.setId(id);
                        CustomerViewModel.updateCustomer(customerEntity);
                    }
                }
            });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);

        // Setting up views and buttons
        nameTextView = findViewById(R.id.nameCusTextView);
        identificationTextView = findViewById(R.id.identificationCusTextView);
        shopTextView = findViewById(R.id.shopCusTextView);
        registrationTextView = findViewById(R.id.registrationCusTextView);
        phoneTextView = findViewById(R.id.numberCusTextView);
        phoneAltTextView = findViewById(R.id.numberAltCusTextView);
        addressTextView = findViewById(R.id.addressCusTextView);
        locLatLngTextView = findViewById(R.id.locationCusTextView);

        copyLatLngImageButton = findViewById(R.id.copyLatLngImageButton);
        copyNumberImageButton = findViewById(R.id.copyNumberImageButton3);
        copyNumberAltImageButton = findViewById(R.id.copyNumberAltImageButton2);
        listButton = findViewById(R.id.listButton);
        customerImage = findViewById(R.id.customerImageView);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        modifiedDateTime = findViewById(R.id.modDateTextView);


        listButton.setVisibility(View.GONE);


        // Setting texts into appropriate field based on item selected by user
        nameTextView.setText(getIntent().getStringExtra(NAME_TAG));
        identificationTextView.setText(getIntent().getStringExtra(IDENTIFICATION_TAG));
        shopTextView.setText(getIntent().getStringExtra(SHOP_TAG));
        registrationTextView.setText(getIntent().getStringExtra(REGISTRATION_TAG));
        phoneTextView.setText(getIntent().getStringExtra(PHONE_TAG));
        phoneAltTextView.setText(getIntent().getStringExtra(PHONE_ALT_TAG));
        addressTextView.setText(getIntent().getStringExtra(ADDRESS_TAG));
        locLatLngTextView.setText(getIntent().getStringExtra(LOC_LAT_LNG_TAG));
        modifiedDateTime.setText(getIntent().getStringExtra(TIME_TAG));


        if(getIntent().hasExtra(ID_TAG)) {
            id = getIntent().getIntExtra(ID_TAG, 0);
        }

        if(getIntent().getByteArrayExtra(IMAGE_TAG) != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(getIntent().getByteArrayExtra(IMAGE_TAG));
            Bitmap photo = BitmapFactory.decodeStream(bis);
            customerImage.setImageBitmap(photo);
        }

        if(getIntent().getStringExtra(NAME_TAG) != null) {
            name = getIntent().getStringExtra(NAME_TAG);
        }

        if(getIntent().getStringExtra(IDENTIFICATION_TAG) != null) {
            identification = getIntent().getStringExtra(IDENTIFICATION_TAG);
        }

        if(getIntent().getStringExtra(SHOP_TAG) != null) {
            shop = getIntent().getStringExtra(SHOP_TAG);
        }

        if(getIntent().getStringExtra(REGISTRATION_TAG) != null) {
            registration = getIntent().getStringExtra(REGISTRATION_TAG);
        }

        if(getIntent().getStringExtra(ADDRESS_TAG) != null) {
            address = getIntent().getStringExtra(ADDRESS_TAG);
        }

        if(getIntent().getStringExtra(LOC_LAT_LNG_TAG) != null) {
            locLatLng = getIntent().getStringExtra(LOC_LAT_LNG_TAG);
        }

        if(getIntent().getStringExtra(PHONE_TAG) != null) {
            phone = getIntent().getStringExtra(PHONE_TAG);
        }

        if(getIntent().getStringExtra(PHONE_ALT_TAG) != null) {
            phoneAlt = getIntent().getStringExtra(PHONE_ALT_TAG);
        }




        // Setting image button click listeners
        // Copy latitude and longitude entered previously to be used in Maps
        copyLatLngImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locLatLng != null) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("latLngCopy", locLatLng);
                    clipboardManager.setPrimaryClip(clipData);
                }
            }
        });


        // Copy phone number to clipboard
        copyNumberImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone != null) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("phoneCopy", phone);
                    clipboardManager.setPrimaryClip(clipData);
                }
            }
        });


        // Copy alternative number to clipboard
        copyNumberAltImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneAlt != null) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("phoneAltCopy", phoneAlt);
                    clipboardManager.setPrimaryClip(clipData);
                }
            }
        });





        // This method below will straight away call the number when the view is clicked
        if(getIntent().getStringExtra(PHONE_TAG) != null && !getIntent().getStringExtra(PHONE_TAG).equals("-")) {
            phoneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkCallAccess()) {
                        // Call number once view is clicked, use Intent.ACTION_CALL
                        // Use Intent.ACTION_VIEW to copy number to dialer
                        // Intent intent = new Intent(Intent.ACTION_CALL);
                        // Must add "tel:" in Uri.parse to prevent error
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String noDash = "";
                        if(phone.contains("-") && phone.length() > 2) {
                            noDash = phone.replace('-', ' ');
                            intent.setData(Uri.parse("tel:" + noDash));
                        } else {
                            intent.setData(Uri.parse("tel:" + phone));
                        }
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        requestCallPermission();
                    }
                }
            });
        }


        // Long press on number will redirect to WhatsApp chat
        if(!phoneTextView.getText().toString().trim().equals("-")) {
            phoneTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String url = "https://wa.me/6" + phoneTextView.getText().toString().trim();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return false;
                }
            });
        }






        // This method below will straight away call the number when the view is clicked
        if(getIntent().getStringExtra(PHONE_ALT_TAG) != null && !getIntent().getStringExtra(PHONE_ALT_TAG).equals("-")) {
            phoneAltTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkCallAccess()) {
                        // Call number once view is clicked, use Intent.ACTION_CALL
                        // Use Intent.ACTION_VIEW to copy number to dialer
//                        Intent intent = new Intent(Intent.ACTION_CALL);
                        // Must add "tel:" in Uri.parse to prevent error
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String noDash = "";
                        if(phoneAlt.contains("-") && phoneAlt.length() > 2) {
                            noDash = phoneAlt.replace('-', ' ');
                            intent.setData(Uri.parse("tel:" + noDash));
                        } else {
                            intent.setData(Uri.parse("tel:" + phoneAlt));
                        }
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phoneAlt));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        requestCallPermission();
                    }
                }
            });
        }



        if(!phoneAltTextView.getText().toString().trim().equals("-")) {
            phoneAltTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String url = "https://wa.me/6" + phoneAltTextView.getText().toString().trim();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return false;
                }
            });
        }




        // This on click listener will launch Google Maps and key in the given latitude and longitude
        if(!locLatLngTextView.getText().toString().trim().equals("N/A")) {
            locLatLngTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // This method will drop a pin on the destination on Google Maps
                    String[] location = locLatLng.split(",");
                    String latitude = location[0];
                    String longitude = location[1];
                    String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }

                    // This will fire up Google Maps without destination drop pin
//                Uri gmmIntentUri = Uri.parse("geo:" + locLatLng);
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(mapIntent);
//                }
                }
            });
        }



        // When an image is present, clicking on it will enlarge and will be zoomable
        if(getIntent().getByteArrayExtra(IMAGE_TAG) != null) {
            customerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(ViewCustomer.this);
                    dialog.setContentView(R.layout.simple_camera);

                    ImageView imageEnlarge = dialog.findViewById(R.id.enlarge_image);

                    // Get the applied image from imageView as a bitmap
                    Bitmap image = ((BitmapDrawable) customerImage.getDrawable()).getBitmap();

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









        // Creating variable for BiometricManager
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            // This means biometric sensor is enabled
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(ViewCustomer.this, "Login via fingerprint or key in password", Toast.LENGTH_SHORT).show();
                break;
            // This means there is no biometric support for the device
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(ViewCustomer.this, "Biometric not supported on your device", Toast.LENGTH_SHORT).show();
                break;
            // This means biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(ViewCustomer.this, "Biometric currently not available", Toast.LENGTH_LONG).show();
                break;
            // This means biometric sensor does not contain any fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(ViewCustomer.this, "No saved fingerprint found", Toast.LENGTH_LONG).show();
                break;
        }




        // Creating variable for executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // Result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(ViewCustomer.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // When AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Passing data into edit page
                Toast.makeText(ViewCustomer.this, "Login successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ViewCustomer.this, AddEditCustomer.class);
                intent.putExtra(ID_TAG, id);
                intent.putExtra(NAME_TAG, name);
                intent.putExtra(IDENTIFICATION_TAG, identification);
                intent.putExtra(PHONE_TAG, phone);
                intent.putExtra(PHONE_ALT_TAG, phoneAlt);
                intent.putExtra(SHOP_TAG, shop);
                intent.putExtra(REGISTRATION_TAG, registration);
                intent.putExtra(LOC_LAT_LNG_TAG, locLatLng);
                intent.putExtra(ADDRESS_TAG, address);
                Log.d(TAG, "onAuthenticationSucceeded: " + id);
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







        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });







        // Creating variable for executor
        Executor executor2 = ContextCompat.getMainExecutor(this);
        // Result of AUTHENTICATION
        final BiometricPrompt biometricPrompt2 = new BiometricPrompt(ViewCustomer.this, executor2, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // When AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Getting all the info to be deleted as biometric match
                id = getIntent().getIntExtra(ID_TAG, 0);
                name = getIntent().getStringExtra(NAME_TAG);
                identification = getIntent().getStringExtra(IDENTIFICATION_TAG);
                shop = getIntent().getStringExtra(SHOP_TAG);
                registration = getIntent().getStringExtra(REGISTRATION_TAG);
                address = getIntent().getStringExtra(ADDRESS_TAG);
                locLatLng = getIntent().getStringExtra(LOC_LAT_LNG_TAG);
                phone = getIntent().getStringExtra(PHONE_TAG);
                phoneAlt = getIntent().getStringExtra(PHONE_ALT_TAG);
                time = getIntent().getStringExtra(TIME_TAG);
                image = getIntent().getByteArrayExtra(IMAGE_TAG);

                CustomerEntity customerEntity = new CustomerEntity(name, identification, shop, registration,
                        phone, phoneAlt, address, locLatLng, time, image);
                customerEntity.setId(id);
                Log.d(TAG, "onAuthenticationSucceeded: to delete");
                CustomerViewModel.deleteCustomer(customerEntity);
//                CustomerViewModel.deleteCustomerViaID(id);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });



        // Set delete button click listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt2.authenticate(promptInfo);
            }
        });






    }



    // Check for permission to use dialer
    private boolean checkCallAccess() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }


    // Ask user for permission to use dialer
    private void requestCallPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}
                , PERMISSION_ID);
    }


}