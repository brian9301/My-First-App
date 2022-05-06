package com.keane9301.myapp001;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.keane9301.myapp001.Database.Customers.CustomerRoom;
import com.keane9301.myapp001.Database.Lubes.LubeRoom;
import com.keane9301.myapp001.Database.Parts.PartRoom;
import com.keane9301.myapp001.ui.home.AddEditLube;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RestoreDatabase extends AppCompatActivity {
    private Button restoreButton;
    private int PERMISSION_ID = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_database);

        restoreButton = findViewById(R.id.restoreButton);

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
                        Toast.makeText(RestoreDatabase.this, "Lubes database successfully restored.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(RestoreDatabase.this, "Lubes database file was not found.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RestoreDatabase.this, "Lubes database restore error.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RestoreDatabase.this, "Parts database successfully restored.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(RestoreDatabase.this, "Parts database file was not found.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RestoreDatabase.this, "Parts database restore error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RestoreDatabase.this, "Customers database successfully restored.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(RestoreDatabase.this, "Customers database file was not found.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RestoreDatabase.this, "Customers database restore error", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    requestForPermissions();
                }
            }
        });
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