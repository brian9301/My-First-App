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

import com.google.android.material.snackbar.Snackbar;
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

public class BackupDatabase extends AppCompatActivity {
    private Button backupButton;
    private int PERMISSION_ID = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_database);

        backupButton = findViewById(R.id.backupButton);

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
                        deleteBackup(toCopy, new String[]{copyLube, copyLubeShm, copyLubeWal, copyPart, copyPartShm, copyPartWal});
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
                        Toast.makeText(BackupDatabase.this, "Lubes database successfully backup.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(BackupDatabase.this, "Lubes database does not exist.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(BackupDatabase.this, "Lubes database backup error.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BackupDatabase.this, "Parts database successfully backup.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(BackupDatabase.this, "Parts database does not exist.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(BackupDatabase.this, "Parts database backup error.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BackupDatabase.this, "Customers database successfully backup.", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(BackupDatabase.this, "Customers database does not exist.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(BackupDatabase.this, "Customers database backup error.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    requestForPermissions();
                }


            }
        });
    }








    static int MAXIMUM_DATABASE_FILE = 9;
    private void deleteBackup(File directory, String[] pack) {
        File currentLubeDB = new File(pack[0]);
        File currentLubeShmDB = new File(pack[1]);
        File currentLubeWalDB = new File(pack[2]);
        File currentPartDB = new File(pack[3]);
        File currentPartShmDB = new File(pack[4]);
        File currentPartWalDB = new File(pack[5]);

        int fileIndex = -1;
        long lastModTime = System.currentTimeMillis();

        // Check existence of database
        if(!currentLubeDB.exists() && !currentLubeShmDB.exists() && !currentLubeWalDB.exists() &&
                !currentPartDB.exists() && !currentPartShmDB.exists() && !currentPartWalDB.exists()) {
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
                        Toast.makeText(BackupDatabase.this, "Old backups have been replaced by newly backup data", Toast.LENGTH_SHORT).show();
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