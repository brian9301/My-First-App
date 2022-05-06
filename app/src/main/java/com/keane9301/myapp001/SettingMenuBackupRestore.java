package com.keane9301.myapp001;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Entity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.keane9301.myapp001.Database.Customers.CustomerEntity;
import com.keane9301.myapp001.Database.Customers.CustomerRoom;
import com.keane9301.myapp001.Database.Customers.CustomerViewModel;
import com.keane9301.myapp001.Database.Lubes.LubeEntity;
import com.keane9301.myapp001.Database.Lubes.LubeRoom;
import com.keane9301.myapp001.Database.Parts.PartRoom;
import com.keane9301.myapp001.ui.home.AddEditLube;
import com.keane9301.myapp001.ui.slideshow.AddEditCustomer;
import com.keane9301.myapp001.ui.slideshow.ViewCustomer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.Set;
import java.util.concurrent.Executor;

public class SettingMenuBackupRestore extends AppCompatActivity {
    private Button backupButton;
    private Button restoreButton;
    private int PERMISSION_ID = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_menu_backup_restore);

        backupButton = findViewById(R.id.backupButton);
        restoreButton = findViewById(R.id.restoreButton);




        // Creating variable for BiometricManager
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            // This means biometric sensor is enabled
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(SettingMenuBackupRestore.this, "Login via fingerprint or key in password", Toast.LENGTH_SHORT).show();
                break;
            // This means there is no biometric support for the device
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(SettingMenuBackupRestore.this, "Biometric not supported on your device", Toast.LENGTH_SHORT).show();
                break;
            // This means biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(SettingMenuBackupRestore.this, "Biometric currently not available", Toast.LENGTH_LONG).show();
                break;
            // This means biometric sensor does not contain any fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(SettingMenuBackupRestore.this, "No saved fingerprint found", Toast.LENGTH_LONG).show();
                break;
        }




        // Creating variable for executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // Result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(SettingMenuBackupRestore.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // When AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

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






        // Creating variable for executor
        Executor executor2 = ContextCompat.getMainExecutor(this);
        // Result of AUTHENTICATION
        final BiometricPrompt biometricPrompt2 = new BiometricPrompt(SettingMenuBackupRestore.this, executor2, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // When AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });














        // Set up backup for the app and requesting permission to read and write storage
        backupButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                if (checkPermissionAccess()) {
                    // Get the database on the phone
                    File lubeFile = v.getContext().getDatabasePath(LubeRoom.lubeDatabase);
                    File lubeFileShm = new File(lubeFile.getParent(), LubeRoom.lubeDatabase + "-shm");
                    File lubeFileWal = new File(lubeFile.getParent(), LubeRoom.lubeDatabase + "-wal");

                    File partFile = v.getContext().getDatabasePath(PartRoom.partDatabase);
                    File partFileShm = new File(partFile.getParent(), PartRoom.partDatabase + "-shm");
                    File partFileWal = new File(partFile.getParent(), PartRoom.partDatabase + "-wal");

                    File customerFile = v.getContext().getDatabasePath(CustomerRoom.customerDatabase);
                    File customerFileShm = new File(customerFile.getParent(), CustomerRoom.customerDatabase + "-shm");
                    File customerFileWal = new File(customerFile.getParent(), CustomerRoom.customerDatabase + "-wal");


                    // Selecting directory to backup database
                    File toCopy = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Database Backup");

                    // Creating filename for database
                    String copyLube = toCopy.getPath() + File.separator + LubeRoom.lubeDatabase;
                    String copyLubeShm = toCopy.getPath() + File.separator + LubeRoom.lubeDatabase + "-shm";
                    String copyLubeWal = toCopy.getPath() + File.separator + LubeRoom.lubeDatabase + "-wal";

                    String copyPart = toCopy.getPath() + File.separator + PartRoom.partDatabase;
                    String copyPartShm = toCopy.getPath() + File.separator + PartRoom.partDatabase + "-shm";
                    String copyPartWal = toCopy.getPath() + File.separator + PartRoom.partDatabase + "-wal";

                    String copyCustomer = toCopy.getPath() + File.separator + CustomerRoom.customerDatabase;
                    String copyCustomerShm = toCopy.getPath() + File.separator + CustomerRoom.customerDatabase + "-shm";
                    String copyCustomerWal = toCopy.getPath() + File.separator + CustomerRoom.customerDatabase + "-wal";



                    // Check if the directory to save backup files exists
                    Log.d(AddEditLube.TAG, "onClick: ready to backup");
                    String[] pack = new String[]{copyLube, copyLubeShm, copyLubeWal, copyPart, copyPartShm, copyPartWal};
                    if (!toCopy.exists()) {
                        toCopy.mkdirs();
                    } else {
                        deleteBackup(toCopy, new String[]{copyLube, copyLubeShm, copyLubeWal, copyPart, copyPartShm, copyPartWal,
                                    copyCustomer, copyCustomerShm, copyCustomerWal});
                    }

                    // Setup empty file for each database
                    Log.d(AddEditLube.TAG, "onClick: before try loop 1");
                    File createLubeDB = new File(copyLube);
                    File createLubeShmDB = new File(copyLubeShm);
                    File createLubeWalDB = new File(copyLubeWal);
                    try {
                        // Create empty file for each database
                        createLubeDB.createNewFile();
                        createLubeShmDB.createNewFile();
                        createLubeWalDB.createNewFile();
                        // Set buffer
                        int bufferSize = 1024 * 8;
                        byte[] buffer = new byte[bufferSize];
                        int bytesRead = bufferSize;
                        // Get phone's database to copy for backup
                        OutputStream lubeDB = new FileOutputStream(copyLube);
                        InputStream lubeDBInput = new FileInputStream(lubeFile);
                        OutputStream lubeShmDB = new FileOutputStream(copyLubeShm);
                        InputStream lubeShmDBInput = new FileInputStream(lubeFileShm);
                        OutputStream lubeWalDB = new FileOutputStream(copyLubeWal);
                        InputStream lubeWalDBInput = new FileInputStream(lubeFileWal);
                        Log.d(AddEditLube.TAG, "onClick: before while loop 1a");
                        // Copy each database from phone to backup folder if not empty
                        while ((bytesRead = lubeDBInput.read(buffer, 0, bufferSize)) > 0) {
                            lubeDB.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: before try loop 1b");
                        while ((bytesRead = lubeShmDBInput.read(buffer, 0, bufferSize)) > 0) {
                            lubeShmDB.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: before try loop 1c");
                        while ((bytesRead = lubeWalDBInput.read(buffer, 0, bufferSize)) > 0) {
                            lubeWalDB.write(buffer, 0, bytesRead);
                        }
                        // Close database after done backup
                        lubeDB.flush();
                        lubeShmDB.flush();
                        lubeWalDB.flush();
                        lubeDBInput.close();
                        lubeShmDBInput.close();
                        lubeWalDBInput.close();
                        lubeDB.close();
                        lubeShmDB.close();
                        lubeWalDB.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Create empty files for each individual database to backup
                    Log.d(AddEditLube.TAG, "onClick: before try loop 2");
                    File createPartDB = new File(copyPart);
                    File createPartShmDB = new File(copyPartShm);
                    File createPartWalDB = new File(copyPartWal);
                    try {
                        // Create the file
                        createPartDB.createNewFile();
                        createPartShmDB.createNewFile();
                        createPartWalDB.createNewFile();
                        // Set buffer size
                        int bufferSize = 1024 * 8;
                        byte[] buffer = new byte[bufferSize];
                        int bytesRead = bufferSize;
                        // Ready phone's database for copying as backup
                        OutputStream partDB = new FileOutputStream(copyPart);
                        InputStream partDBInput = new FileInputStream(partFile);
                        OutputStream partShmDB = new FileOutputStream(copyPartShm);
                        InputStream partShmDBInput = new FileInputStream(partFileShm);
                        OutputStream partWalDB = new FileOutputStream(copyPartWal);
                        InputStream partWalDBInput = new FileInputStream(partFileWal);
                        Log.d(AddEditLube.TAG, "onClick: before try loop 2a");
                        // Copy each database if its not empty
                        while ((bytesRead = partDBInput.read(buffer, 0, bufferSize)) > 0) {
                            partDB.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: before try loop 2b");
                        while ((bytesRead = partShmDBInput.read(buffer, 0, bufferSize)) > 0) {
                            partShmDB.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: before try loop 2c");
                        while ((bytesRead = partWalDBInput.read(buffer, 0, bufferSize)) > 0) {
                            partWalDB.write(buffer, 0, bytesRead);
                            Log.d(AddEditLube.TAG, "onClick: partWalDB successfully backup");
                        }
                        // Close database after done reading and writing
                        partDB.flush();
                        partShmDB.flush();
                        partWalDB.flush();
                        partDBInput.close();
                        partShmDBInput.close();
                        partWalDBInput.close();
                        partDB.close();
                        partShmDB.close();
                        partWalDB.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // Create empty files for each individual database to backup
                    Log.d(AddEditLube.TAG, "onClick: before try loop 3");
                    File createCustomerDB = new File(copyCustomer);
                    File createCustomerShmDB = new File(copyCustomerShm);
                    File createCustomerWalDB = new File(copyCustomerWal);
                    try {
                        // Create the file
                        createCustomerDB.createNewFile();
                        createCustomerShmDB.createNewFile();
                        createCustomerWalDB.createNewFile();
                        // Set buffer size
                        int bufferSize = 1024 * 8;
                        byte[] buffer = new byte[bufferSize];
                        int bytesRead = bufferSize;
                        // Ready phone's database for copying as backup
                        OutputStream customerDB = new FileOutputStream(copyCustomer);
                        InputStream customerDBInput = new FileInputStream(customerFile);
                        OutputStream customerShmDB = new FileOutputStream(copyCustomerShm);
                        InputStream customerShmDBInput = new FileInputStream(customerFileShm);
                        OutputStream customerWalDB = new FileOutputStream(copyCustomerWal);
                        InputStream customerWalDBInput = new FileInputStream(customerFileWal);
                        Log.d(AddEditLube.TAG, "onClick: before try loop 3a");
                        // Copy each database if its not empty
                        while ((bytesRead = customerDBInput.read(buffer, 0, bufferSize)) > 0) {
                            customerDB.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: before try loop 3b");
                        while ((bytesRead = customerShmDBInput.read(buffer, 0, bufferSize)) > 0) {
                            customerShmDB.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: before try loop 3c");
                        while ((bytesRead = customerWalDBInput.read(buffer, 0, bufferSize)) > 0) {
                            customerWalDB.write(buffer, 0, bytesRead);
                            Log.d(AddEditLube.TAG, "onClick: customerWalDB successfully backup");
                        }
                        // Close database after done reading and writing
                        customerDB.flush();
                        customerShmDB.flush();
                        customerWalDB.flush();
                        customerDBInput.close();
                        customerShmDBInput.close();
                        customerWalDBInput.close();
                        customerDB.close();
                        customerShmDB.close();
                        customerWalDB.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    requestForPermissions();
                }
            }
        });








        // Set up restore database and getting permission to storage
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                if (checkPermissionAccess()) {
                    // Getting database files to repopulate
                    File lubeBackupDB = new File(Environment.getExternalStorageDirectory().getPath() + "/Documents/Database Backup/" + LubeRoom.lubeDatabase);
                    File lubeShmBackupDB = new File(lubeBackupDB.getParent(), LubeRoom.lubeDatabase + "-shm");
                    File lubeWalBackupDB = new File(lubeBackupDB.getParent(), LubeRoom.lubeDatabase + "-wal");

                    File partBackupDB = new File(Environment.getExternalStorageDirectory().getPath() + "/Documents/Database Backup/" + PartRoom.partDatabase);
                    File partShmBackupDB = new File(partBackupDB.getParent(), PartRoom.partDatabase + "-shm");
                    File partWalBackupDB = new File(partBackupDB.getParent(), PartRoom.partDatabase + "-wal");

                    File customerBackupDB = new File(Environment.getExternalStorageDirectory().getPath() + "/Documents/Database Backup/" + CustomerRoom.customerDatabase);
                    File customerShmBackupDB = new File(customerBackupDB.getParent(), CustomerRoom.customerDatabase + "-shm");
                    File customerWalBackupDB = new File(customerBackupDB.getParent(), CustomerRoom.customerDatabase + "-wal");

                    // Getting phone database to be overwritten
                    File lubeDB = v.getContext().getDatabasePath(LubeRoom.lubeDatabase);
                    File lubeShmDB = new File(lubeDB.getParent(), LubeRoom.lubeDatabase + "-shm");
                    File lubeWalDB = new File(lubeDB.getParent(), LubeRoom.lubeDatabase + "-wal");

                    File partDB = v.getContext().getDatabasePath(PartRoom.partDatabase);
                    File partShmDB = new File(partDB.getParent(), PartRoom.partDatabase + "-shm");
                    File partWalDB = new File(partDB.getParent(), PartRoom.partDatabase + "-wal");

                    File customerDB = v.getContext().getDatabasePath(CustomerRoom.customerDatabase);
                    File customerShmDB = new File(customerDB.getParent(), CustomerRoom.customerDatabase + "-shm");
                    File customerWalDB = new File(customerDB.getParent(), CustomerRoom.customerDatabase + "-wal");

                    Log.d(AddEditLube.TAG, "onClick: restore try loop 1 " + lubeDB.getAbsolutePath() + "\n"
                            + lubeBackupDB.getAbsolutePath());

                    try {
                        // Set buffer size
                        int bufferSize = 1024 * 8;
                        byte[] buffer = new byte[bufferSize];
                        int bytesRead = bufferSize;
                        // Get the preloaded database ready to populate the phone's database
                        Log.d(AddEditLube.TAG, "onClick: Ori lube DB");
                        OutputStream DBLube = new FileOutputStream(lubeDB);
                        InputStream DBInputLube = new FileInputStream(lubeBackupDB);
                        Log.d(AddEditLube.TAG, "onClick: Ori lubeShm DB");
                        OutputStream DBShmLube = new FileOutputStream(lubeShmDB);
                        InputStream DBShmInputLube = new FileInputStream(lubeShmBackupDB);
                        Log.d(AddEditLube.TAG, "onClick: Ori lubeWal DB");
                        OutputStream DBWalLube = new FileOutputStream(lubeWalDB);
                        InputStream DBWalInputLube = new FileInputStream(lubeWalBackupDB);
                        Log.d(AddEditLube.TAG, "onClick: restore while 1");
                        // Overriding phone's database with preloaded database from previous backup
                        // Or from other devices
                        while ((bytesRead = DBInputLube.read(buffer, 0, bufferSize)) > 0) {
                            DBLube.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: restore while 2");
                        while ((bytesRead = DBShmInputLube.read(buffer, 0, bufferSize)) > 0) {
                            DBShmLube.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: restore while 3");
                        while ((bytesRead = DBWalInputLube.read(buffer, 0, bufferSize)) > 0) {
                            DBWalLube.write(buffer, 0, bytesRead);
                        }
                        // Close all database after done reading and writing
                        DBLube.flush();
                        DBShmLube.flush();
                        DBWalLube.flush();
                        DBInputLube.close();
                        DBShmInputLube.close();
                        DBWalInputLube.close();
                        DBLube.close();
                        DBShmLube.close();
                        DBWalLube.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Log.d(AddEditLube.TAG, "onClick: restore try loop 2");
                    try {
                        // Set buffer to read database
                        int bufferSize = 1024 * 8;
                        byte[] buffer = new byte[bufferSize];
                        int bytesRead = bufferSize;
                        // Ready preloaded database and phone's database for replacement
                        OutputStream DBPart = new FileOutputStream(partDB);
                        InputStream DBInputPart = new FileInputStream(partBackupDB);
                        OutputStream DBShmPart = new FileOutputStream(partShmDB);
                        InputStream DBShmInputPart = new FileInputStream(partShmBackupDB);
                        OutputStream DBWalPart = new FileOutputStream(partWalDB);
                        InputStream DBWalInputPart = new FileInputStream(partWalBackupDB);
                        Log.d(AddEditLube.TAG, "onClick: restore while 4");
                        // Replacing phone's database with external backup data
                        while ((bytesRead = DBInputPart.read(buffer, 0, bufferSize)) > 0) {
                            DBPart.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: restore while 5");
                        while ((bytesRead = DBShmInputPart.read(buffer, 0, bufferSize)) > 0) {
                            DBShmPart.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: restore while 6");
                        while ((bytesRead = DBWalInputPart.read(buffer, 0, bufferSize)) > 0) {
                            DBWalPart.write(buffer, 0, bytesRead);
                        }
                        // CLosing connection for all databases after done reading and writing
                        DBPart.flush();
                        DBShmPart.flush();
                        DBWalPart.flush();
                        DBInputPart.close();
                        DBShmInputPart.close();
                        DBWalInputPart.close();
                        DBPart.close();
                        DBShmPart.close();
                        DBWalPart.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Log.d(AddEditLube.TAG, "onClick: restore try loop 3");
                    try {
                        // Set buffer to read database
                        int bufferSize = 1024 * 8;
                        byte[] buffer = new byte[bufferSize];
                        int bytesRead = bufferSize;
                        // Ready preloaded database and phone's database for replacement
                        OutputStream DBCustomer = new FileOutputStream(customerDB);
                        InputStream DBInputCustomer = new FileInputStream(customerBackupDB);
                        OutputStream DBShmCustomer = new FileOutputStream(customerShmDB);
                        InputStream DBShmInputCustomer = new FileInputStream(customerShmBackupDB);
                        OutputStream DBWalCustomer = new FileOutputStream(customerWalDB);
                        InputStream DBWalInputCustomer = new FileInputStream(customerWalBackupDB);
                        Log.d(AddEditLube.TAG, "onClick: restore while 7");
                        // Replacing phone's database with external backup data
                        while ((bytesRead = DBInputCustomer.read(buffer, 0, bufferSize)) > 0) {
                            DBCustomer.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: restore while 8");
                        while ((bytesRead = DBShmInputCustomer.read(buffer, 0, bufferSize)) > 0) {
                            DBShmCustomer.write(buffer, 0, bytesRead);
                        }
                        Log.d(AddEditLube.TAG, "onClick: restore while 9");
                        while ((bytesRead = DBWalInputCustomer.read(buffer, 0, bufferSize)) > 0) {
                            DBWalCustomer.write(buffer, 0, bytesRead);
                        }
                        // CLosing connection for all databases after done reading and writing
                        DBCustomer.flush();
                        DBShmCustomer.flush();
                        DBWalCustomer.flush();
                        DBInputCustomer.close();
                        DBShmInputCustomer.close();
                        DBWalInputCustomer.close();
                        DBCustomer.close();
                        DBShmCustomer.close();
                        DBWalCustomer.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    requestForPermissions();
                }
            }
        });




    }



    static int MAXIMUM_DATABASE_FILE = 6;
    private void deleteBackup(File directory, String[] pack) {
        File currentLubeDB = new File(pack[0]);
        File currentLubeShmDB = new File(pack[1]);
        File currentLubeWalDB = new File(pack[2]);
        File currentPartDB = new File(pack[3]);
        File currentPartShmDB = new File(pack[4]);
        File currentPartWalDB = new File(pack[5]);
        File currentCustomerDB = new File(pack[6]);
        File currentCustomerShmDB = new File(pack[7]);
        File currentCustomerWalDB = new File(pack[8]);

        int fileIndex = -1;
        long lastModTime = System.currentTimeMillis();

        // Check existence of database
        if(!currentLubeDB.exists() && !currentLubeShmDB.exists() && !currentLubeWalDB.exists() &&
            !currentPartDB.exists() && !currentPartShmDB.exists() && !currentPartWalDB.exists() &&
            !currentCustomerDB.exists() && !currentCustomerShmDB.exists() && !currentCustomerWalDB.exists()) {
            File[] files = directory.listFiles();
            if(files != null && files.length > MAXIMUM_DATABASE_FILE) {
                for(int i = 0; i < files.length; i++) {
                    File file = files[i];
                    long fileLastModTime = file.lastModified();
                    // Proceed backup if file is older than current ones
                    if(fileLastModTime < lastModTime) {
                        lastModTime = fileLastModTime;
                        fileIndex = 1;
                    }
                }

                if(fileIndex != -1) {
                    File deleteFile = files[fileIndex];
                    // Delete old database backup
                    if(deleteFile.exists()) {
                        deleteFile.delete();
                    }
                }
            }
        }
    }





    // Checking for permission granted
    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean checkPermissionAccess() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
    }



    // Asking for permission from user
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestForPermissions() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSION_ID);
    }

}