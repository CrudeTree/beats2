package com.larjam.beats2.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.larjam.beats2.R;

public class BackColorsSettingsActivity extends AppCompatActivity {

  private static final String LOG_TAG = "SettingsActivity";
  private SeekBar seekBarRed;
  private SeekBar seekBarBlue;
  private SeekBar seekBarGreen;
  private AppBarLayout appBarColor;
  private Toolbar toolbarColor;
  private View backColor;
  private Button saveButton;
  private int updatedGreenBack;
  private int updatedRedBack;
  private int updatedBlueBack;
  private int updatedGreen;
  private int updatedRed;
  private int updatedBlue;
  private int red;
  private int green;
  private int blue;
  private int redBack;
  private int greenBack;
  private int blueBack;
  private TextView backColorText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_back_colors_settings);
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

    seekBarRed = findViewById(R.id.seek_bar_red);
    seekBarGreen = findViewById(R.id.seek_bar_green);
    seekBarBlue = findViewById(R.id.seek_bar_blue);

    seekBarRed.setProgress(pref.getInt("redBackProgress", 50));
    seekBarGreen.setProgress(pref.getInt("greenBackProgress", 50));
    seekBarBlue.setProgress(pref.getInt("blueBackProgress", 50));

    seekBarRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        updatedRedBack = (int) Math.ceil(254 * (seekBarRed.getProgress() / 100d));
        editor.putInt("redBack", updatedRedBack);
        editor.putInt("redBackProgress", seekBarRed.getProgress());
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
          boolean fromUser) {
        backColor = findViewById(R.id.back_color);

        redBack = (int) Math.ceil(254 * (seekBarRed.getProgress() / 100d));

        backColor.setBackgroundColor(Color.rgb(redBack,greenBack,blueBack));
      }
    });

    seekBarGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        updatedGreenBack = (int) Math.ceil(254 * (seekBar.getProgress() / 100d));
        editor.putInt("greenBack", updatedGreenBack);
        editor.putInt("greenBackProgress", seekBarGreen.getProgress());

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
          boolean fromUser) {
        backColor = findViewById(R.id.back_color);

        greenBack = (int) Math.ceil(254 * (seekBarGreen.getProgress() / 100d));

        backColor.setBackgroundColor(Color.rgb(redBack,greenBack,blueBack));
      }
    });

    seekBarBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        updatedBlueBack = (int) Math.ceil(254 * (seekBar.getProgress() / 100d));
        editor.putInt("blueBack", updatedBlueBack);
        editor.putInt("blueBackProgress", seekBarBlue.getProgress());

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
          boolean fromUser) {
        backColor = findViewById(R.id.back_color);

        blueBack = (int) Math.ceil(254 * (seekBarBlue.getProgress() / 100d));

        backColor.setBackgroundColor(Color.rgb(redBack,greenBack,blueBack));
      }
    });
  }

  private void colorPreference() {
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    seekBarRed = findViewById(R.id.seek_bar_red);
    seekBarGreen = findViewById(R.id.seek_bar_green);
    seekBarBlue = findViewById(R.id.seek_bar_blue);
    seekBarRed.setProgress(pref.getInt("redBackProgress", 50));
    seekBarGreen.setProgress(pref.getInt("greenBackProgress", 50));
    seekBarBlue.setProgress(pref.getInt("blueBackProgress", 50));
    redBack = pref.getInt("redBack", 254);
    greenBack = pref.getInt("greenBack", 254);
    blueBack = pref.getInt("blueBack", 254);
    red = pref.getInt("red", 11);
    green = pref.getInt("green", 60);
    blue = pref.getInt("blue", 73);
    toolbarColor = findViewById(R.id.toolbar);
    appBarColor = findViewById(R.id.appbar);
    toolbarColor.setBackgroundColor(Color.rgb(red, green, blue));
    appBarColor.setBackgroundColor(Color.rgb(red, green, blue));
    backColor = findViewById(R.id.back_color);
    backColor.setBackgroundColor(Color.rgb(redBack, greenBack, blueBack));
    backColorText = findViewById(R.id.front_colors);
    backColorText.setTextColor(Color.rgb(red, green, blue));
  }


}
