package com.larjam.beats2.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.larjam.beats2.R;

public class FrontColorsSettingsActivity extends AppCompatActivity {

  private static final String LOG_TAG = "SettingsActivity";
  private SeekBar seekBarRed;
  private SeekBar seekBarBlue;
  private SeekBar seekBarGreen;
  private AppBarLayout appBarColor;
  private Toolbar toolbarColor;
  private TextView frontColorText;
  private Button saveButton;
  private int updatedGreen;
  private int updatedRed;
  private int updatedBlue;
  private int red;
  private int green;
  private int blue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_front_colors_settings);
    SharedPreferences pref = getApplicationContext()
        .getSharedPreferences("MyPref", 0); // 0 - for private mode
    Editor editor = pref.edit();
    colorPreference();

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    saveButton = findViewById(R.id.save_button);
    saveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        editor.commit();

        Toast.makeText(getApplicationContext(), "Settings Saved", Toast.LENGTH_SHORT).show();
      }
    });

    frontColorText = findViewById(R.id.front_colors);
    seekBarRed = findViewById(R.id.seek_bar_red);
    seekBarGreen = findViewById(R.id.seek_bar_green);
    seekBarBlue = findViewById(R.id.seek_bar_blue);

    seekBarRed.setProgress(pref.getInt("redProgress", 50));
    seekBarGreen.setProgress(pref.getInt("greenProgress", 50));
    seekBarBlue.setProgress(pref.getInt("blueProgress", 50));

    seekBarRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        updatedRed = (int) Math.ceil(254 * (seekBarRed.getProgress() / 100d));
        editor.putInt("red", updatedRed);
        editor.putInt("redProgress", seekBarRed.getProgress());
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
          boolean fromUser) {
        toolbarColor = findViewById(R.id.toolbar);
        appBarColor = findViewById(R.id.appbar);

        red = (int) Math.ceil(254 * (seekBarRed.getProgress() / 100d));

        frontColorText.setTextColor(Color.rgb(red,green,blue));
        toolbarColor.setBackgroundColor(Color.rgb(red, green, blue));
        appBarColor.setBackgroundColor(Color.rgb(red, green, blue));
      }
    });

    seekBarGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        updatedGreen = (int) Math.ceil(254 * (seekBar.getProgress() / 100d));
        editor.putInt("green", updatedGreen);
        editor.putInt("greenProgress", seekBarGreen.getProgress());

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
          boolean fromUser) {
        toolbarColor = findViewById(R.id.toolbar);
        appBarColor = findViewById(R.id.appbar);

        green = (int) Math.ceil(254 * (seekBarGreen.getProgress() / 100d));

        frontColorText.setTextColor(Color.rgb(red,green,blue));
        toolbarColor.setBackgroundColor(Color.rgb(red, green, blue));
        appBarColor.setBackgroundColor(Color.rgb(red, green, blue));
      }
    });

    seekBarBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        updatedBlue = (int) Math.ceil(254 * (seekBar.getProgress() / 100d));
        editor.putInt("blue", updatedBlue);
        editor.putInt("blueProgress", seekBarBlue.getProgress());

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
          boolean fromUser) {
        toolbarColor = findViewById(R.id.toolbar);
        appBarColor = findViewById(R.id.appbar);

        blue = (int) Math.ceil(254 * (seekBarBlue.getProgress() / 100d));

        frontColorText.setTextColor(Color.rgb(red,green,blue));
        toolbarColor.setBackgroundColor(Color.rgb(red, green, blue));
        appBarColor.setBackgroundColor(Color.rgb(red, green, blue));
      }
    });
  }


  private void colorPreference() {
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    Editor editor = pref.edit();
    red = pref.getInt("red", 11);
    green = pref.getInt("green", 60);
    blue = pref.getInt("blue", 73);
    toolbarColor = findViewById(R.id.toolbar);
    appBarColor = findViewById(R.id.appbar);
    toolbarColor.setBackgroundColor(Color.rgb(red, green, blue));
    appBarColor.setBackgroundColor(Color.rgb(red, green, blue));
  }


}
