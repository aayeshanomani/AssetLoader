package com.example.loadasset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import lib.folderpicker.FolderPicker;

public class MainActivity extends AppCompatActivity {


    public static final int FOLDERPICKER_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context appContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loadFileBut = findViewById(R.id.loadfileBut);

        loadFileBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("click", "hey");
                String[] PERMISSIONS = {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (hasPermissions(MainActivity.this, PERMISSIONS)) {
                    File file = new File(getExternalCacheDir(), "sample.txt");
                    if (file.exists()) {
                        System.out.println("File Exist");
                    } else {
                        System.out.println("File NOT Exist");
                        copyAssets();
                    }
                } else{
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, FOLDERPICKER_PERMISSIONS);
                }



            }
        });


    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    static InputStream in = null;
    static OutputStream out = null;

    private void copyAssets() {
        AssetManager assetManager = getAssets();



                /*Intent intent = new Intent(this, FolderPicker.class);
                // 1. Initialize dialog
                final StorageChooser chooser = new StorageChooser.Builder()
                        // Specify context of the dialog
                        .withActivity(MainActivity.this)
                        .withFragmentManager(getFragmentManager())
                        .withMemoryBar(true)
                        .allowCustomPath(true)
                        // Define the mode as the FOLDER/DIRECTORY CHOOSER
                        .setType(StorageChooser.DIRECTORY_CHOOSER)
                        .build();

// 2. Handle what should happend when the user selects the directory !
                chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
                    @Override
                    public void onSelect(String path) {
                                                }
                    }

                });

// 3. Display File Picker whenever you need to !
                chooser.show();
                //File outFile = new File(getExternalFilesDir(null), filename);

            }




    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }*/


        new ChooserDialog(MainActivity.this)
                .withFilter(true, false)
                .withStartFile("")
                // to handle the result(s)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String path, File pathFile) {
                        Toast.makeText(MainActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                        String[] files = null;
                        try {
                            files = assetManager.list("");
                        } catch (IOException e) {
                            Log.e("tag", "Failed to get asset file list.", e);
                        }
                        if (files != null) for (String filename : files) {
                            try {

                                in = assetManager.open(filename);
                                File outFile = new File(path, filename);
                                out = new FileOutputStream(outFile);
                                byte[] buffer = new byte[1024];
                                int read;
                                while ((read = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, read);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                in = null;
                            } finally {
                                if (in != null) {
                                    try {
                                        in.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (out != null) {
                                    try {
                                        out.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            Log.d("path", path);

                        }
                    }
                })
                .build()
                .show();
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FOLDERPICKER_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                            MainActivity.this,
                            "Permission granted! Please click on pick a file once again.",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "Permission denied to read your External storage :(",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                return;
            }
        }
    }*/
}