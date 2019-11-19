package com.larjam.beats2.controller;

import android.Manifest.permission;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.codekidlabs.storagechooser.StorageChooser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.larjam.beats2.R;
import java.io.File;

public class FileSettingsActivity extends AppCompatActivity {

  private static final int MY_PERMISSION_REQUEST = 1;
  private static final String TAG = "FileSettings";
  String path;
  Button saveButton;
  Button fileDirectoryButton;
  TextView fileText;
  Editor editor;
  TextView description;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_file_settings);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    if (ContextCompat.checkSelfPermission(FileSettingsActivity.this,
        permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(FileSettingsActivity.this,
          permission.READ_EXTERNAL_STORAGE)) {
        ActivityCompat.requestPermissions(FileSettingsActivity.this,
            new String[]{permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
      } else {
        ActivityCompat.requestPermissions(FileSettingsActivity.this,
            new String[]{permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
      }
    } else {
      doStuff();
    }

  }

  private void doStuff() {
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    editor = pref.edit();
    Intent intent = getIntent();
    String notFirst = intent.getStringExtra("notFirst");

    Log.d(TAG, "is intent.getExtras empty?   " + notFirst);
    Log.d(TAG, "is pref empty?   " + pref.getString("FileDirectory", path));

    //When you start up the app for the very first time
    if(pref.getString("FileDirectory", path) == null ) {
      startFileSettings();
    }

    //When you start the app after previously declaring a audio directory
    if(pref.getString("FileDirectory", path) != null && notFirst == null) {
      intent = new Intent(this, PlayerActivity.class);
      startActivity(intent);
    }

    //When you access the audio directory from settings
    if(pref.getString("FileDirectory", path) != null && notFirst != null) {
      startFileSettings();
    }
  }

  private void startFileSettings() {
    saveButton = findViewById(R.id.save_button);
    fileDirectoryButton = findViewById(R.id.file_directory_button);
    fileText = findViewById(R.id.txt_path);
    description = findViewById(R.id.description);
    Log.d(TAG, "FileText" + fileText);
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

    if(editor.putString("FileDirectory", path) != null) {
      description.setText("This is your file path: " + pref.getString("FileDirectory", path));
    }

    saveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        editor.commit();
        Toast.makeText(FileSettingsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
      }
    });

    fileDirectoryButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        // 1. Initialize dialog
        final StorageChooser chooser = new StorageChooser.Builder()
            // Specify context of the dialog
            .withActivity(FileSettingsActivity.this)
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
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

            editor.putString("FileDirectory", path);
            Log.d(TAG, "GIMME DAT VALUEEEE:    " + path);
            fileText.setText(pref.getString("FileDirectory", path));
          }
        });
        chooser.show();
      }
    });

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSION_REQUEST: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          if (ContextCompat.checkSelfPermission(FileSettingsActivity.this,
              permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            doStuff();
          }
        } else {
          Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
          finish();
        }
        return;
      }
    }
  }

  @Override
  public void onBackPressed() {
    Log.d(TAG, "FileText" + String.valueOf(fileText));
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    if (pref.getString("FileDirectory", path) == null) {
      startActivity(new Intent(this, FileSettingsActivity.class));
    } else {
      startActivity(new Intent(this, PlayerActivity.class));
    }
  }
}