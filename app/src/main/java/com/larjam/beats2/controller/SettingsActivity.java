package com.larjam.beats2.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.larjam.beats2.R;


public class SettingsActivity extends AppCompatActivity {

  private Button frontColorButton;
  private Button backColorButton;
  private Button fileChooserButton;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    fileChooserButton = findViewById(R.id.file_directory);
    frontColorButton = findViewById(R.id.front_colors);
    backColorButton = findViewById(R.id.back_colors);

    frontColorButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        openFrontColorsSettingsActivity();
      }
    });

    backColorButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        openBackColorsSettingsActivity();
      }
    });

    fileChooserButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        openFileSettingsActivity();
      }
    });
  }

  public void openFileSettingsActivity () {
    Intent intent = new Intent(this, FileSettingsActivity.class);
    String notFirst = "notFirst";
    intent.putExtra("notFirst", notFirst);
    startActivity(intent);
  }

  public void openBackColorsSettingsActivity () {
    Intent intent = new Intent(this, BackColorsSettingsActivity.class);
    startActivity(intent);
  }

  public void openFrontColorsSettingsActivity () {
    Intent intent = new Intent(this, FrontColorsSettingsActivity.class);
    startActivity(intent);
  }

  @Override
  public void onBackPressed() {
    startActivity(new Intent(this, PlayerActivity.class));
  }

}
