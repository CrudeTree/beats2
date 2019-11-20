package com.larjam.beats2.controller;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.android.AndroidAudioPlayer;
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;

import be.tarsos.dsp.resample.Resampler;
import com.google.android.material.appbar.AppBarLayout;
import com.larjam.beats2.GoogleSignInService;
import com.larjam.beats2.R;
import com.larjam.beats2.viewmodel.MainViewModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerActivity extends AppCompatActivity {

  private static final String TAG = "PlayerActivity";
  private GoogleSignInService signInService;
  private boolean isPlay = false;
  private boolean loop;
  private Button mPlayButton;
  private Button mNextButton;
  private Button mPreviousButton;
  private MediaPlayer player;
  private MainViewModel viewModel;
  private Uri uri;
  private TextView displaySongIdentifier;
  private SeekBar seekBar;
  private Runnable runnable;
  private Handler handler;
  private ArrayList<String> arrayList;
  private ListView listView;
  private int songIndex;
  private PitchShifter pitchShifter;
  private float[] buffer;
  private AudioDispatcher dispatcher;
  private File file;
  private AppBarLayout appBarColor;
  private Toolbar toolbarColor;
  private TextView pitchText;
  private TextView originalTempoText;
  private TextView keepSettingsText;
  private Button ic_play;
  private MediaPlayer mp;
  private String path;
  private String title;
  private boolean nextSongRequested;
  private View backColor;

  private int redBack;
  private int blueBack;
  private int greenBack;
  private int red;
  private int blue;
  private int green;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

    handler = new Handler();
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();

    listView = findViewById(R.id.listView);
    arrayList = new ArrayList<>();
    colorPreference();
    getMusic();
    mp = MediaPlayer.create(this, R.raw.click_sound);
    mp.setVolume(0.2f, 0.2f);
    mNextButton = findViewById(R.id.next_button);
    mPreviousButton = findViewById(R.id.previous_button);

    mPlayButton = findViewById(R.id.play);
    seekBar = findViewById(R.id.seek_bar);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (arrayList.size() == 0) {
      displaySongIdentifier.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
      displaySongIdentifier.setTextColor(ContextCompat.getColor(this, R.color.errorColor));
      displaySongIdentifier.setText("Choose Valid Directory In Settings");
    } else {
      mNextButton.setOnClickListener(this::nextSong);
      mPreviousButton.setOnClickListener(this::previousSong);

      mPlayButton.setOnClickListener(this::play);
      displaySongIdentifier.setText(arrayList.get(0));

      if (extras != null) {
        songIndex = extras.getInt("songIndex");
        uri = Uri
            .parse(pref.getString("FileDirectory", path) + "/" + arrayList.get(songIndex));
        displaySongIdentifier.setText(arrayList.get(songIndex));
        player = MediaPlayer.create(this, uri);
        if (extras.getBoolean("start")) {
          player = MediaPlayer.create(this, uri);
          play(mPlayButton);
        }
      } else {
        uri = Uri.parse(pref.getString("FileDirectory", path) + "/" + arrayList.get(0));
        player = MediaPlayer.create(this, uri);

      }

      player.setOnPreparedListener(player -> {
        seekBar.setMax(player.getDuration());
        changeSeekbar();
      });

      setupSignIn();
      viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
      Log.d(TAG, "onCreate");

      seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
          if (fromUser) {
            player.seekTo(i);
          }

          if (!nextSongRequested && seekBar.getProgress() >= player.getDuration() * 0.99) {
            nextSongRequested = true;
            seekBar.setProgress(0);
            songIndex += 1;
            songIndex %= arrayList.size();
            File f = new File(arrayList.get(songIndex));
            Log.d(TAG, "NextSong: " + arrayList.get(songIndex));

            Intent intent = new Intent(PlayerActivity.this, PlayerActivity.class);
            Bundle extras = new Bundle();
            extras.putString("title", f.getAbsoluteFile().getName());
            extras.putInt("songIndex", songIndex);
            extras.putBoolean("start", true);
            intent.putExtras(extras);
            startActivity(intent);
          }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
      });
    }
  }

  private void colorPreference() {
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    redBack = pref.getInt("redBack", 50);
    greenBack = pref.getInt("greenBack", 50);
    blueBack = pref.getInt("blueBack", 50);
    red = pref.getInt("red", 11);
    green = pref.getInt("green", 60);
    blue = pref.getInt("blue", 73);
    toolbarColor = findViewById(R.id.toolbar);
    appBarColor = findViewById(R.id.appbar);
    toolbarColor.setBackgroundColor(Color.rgb(red, green, blue));
    appBarColor.setBackgroundColor(Color.rgb(red, green, blue));
    backColor = findViewById(R.id.main_content);
    backColor.setBackgroundColor(Color.rgb(redBack, greenBack, blueBack));
    pitchText = findViewById(R.id.text_pitch);
    pitchText.setTextColor(Color.rgb(red, green, blue));
    displaySongIdentifier = findViewById(R.id.display_song_name);
    displaySongIdentifier.setTextColor(Color.rgb(red, green, blue));
    originalTempoText = findViewById(R.id.original_tempo);
    keepSettingsText = findViewById(R.id.save_settings);
    originalTempoText.setTextColor(Color.rgb(red, green, blue));
    keepSettingsText.setTextColor(Color.rgb(red, green, blue));
  }


  private void startFile(File file) {
    int currentFactor = 4;
    final int size = 2048;
    final int overlap = 2048 - 256;
    int samplerate = 44100;
    new AndroidFFMPEGLocator(this);
    final AudioDispatcher d = AudioDispatcherFactory
        .fromPipe(file.getAbsolutePath(), samplerate, size, overlap);
    pitchShifter = new PitchShifter(1, samplerate, size, overlap);
    pitchShifter.setPitchShiftFactor((float) 1);
    d.addAudioProcessor(pitchShifter);
    d.addAudioProcessor(new AndroidAudioPlayer(new TarsosDSPAudioFormat(samplerate, 16, 1, true, true), 4300, 3));
//    d.addAudioProcessor(new AudioProcessor() {
//      @Override
//      public void processingFinished() {
//
//      }
//
//      @Override
//      public boolean process(AudioEvent audioEvent) {
//        buffer = audioEvent.getFloatBuffer();
//        return true;
//      }
//  });

//    d.addAudioProcessor(new AudioProcessor() {
//      Resampler r = new Resampler(false, 0.1, 4.0);
//
//      @Override
//      public void processingFinished() {
//      }
//
//      @Override
//      public boolean process(AudioEvent audioEvent) {
//
//        float factor = (float) (currentFactor);
//        float[] src = audioEvent.getFloatBuffer();
//        float[] out = new float[(int) ((size - overlap) * factor)];
//        r.process(factor, src, overlap, size - overlap, false, out, 0, out.length);
//        //The size of the output buffer changes (according to factor).
//        d.setStepSizeAndOverlap(out.length, 0);
//
//        audioEvent.setFloatBuffer(out);
//        audioEvent.setOverlap(0);
//
//        return true;
//      }
//    });
//    d.addAudioProcessor(rateTransposer);
//    try {
//      d.addAudioProcessor(new AudioPlayer(d.getFormat()));
//    } catch (LineUnavailableException e) {
//      e.printStackTrace();
//    }
//    d.addAudioProcessor(new AudioProcessor() {
//
//      @Override
//      public void processingFinished() {
//      }
//
//      @Override
//      public boolean process(AudioEvent audioEvent) {
//        d.setStepSizeAndOverlap(size, overlap);
//        d.setAudioFloatBuffer(buffer);
//        audioEvent.setFloatBuffer(buffer);
//        audioEvent.setOverlap(overlap);
//        return true;
//      }
//    });

    Thread t = new Thread(d);
    t.start();

  }


  private void changeSeekbar() {
    if (player != null && player.isPlaying()) {
      seekBar.setProgress(player.getCurrentPosition());

      runnable = this::changeSeekbar;
      handler.postDelayed(runnable, 1000);
    }
  }

  private void setupSignIn() {
    signInService = GoogleSignInService.getInstance();
  }

  private void signOut() {
    signInService.signOut()
        .addOnCompleteListener((task) -> {
          Intent intent = new Intent(this, LoginActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
        });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.playlist:
        mp.start();
        openSongListActivity();
        break;
      case R.id.sign_out:
        mp.start();
        signOut();
        break;
      case R.id.settings:
        mp.start();
        openSettingsActivity();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  public void openSongListActivity() {
    Intent intent = new Intent(this, PlaylistActivity.class);
    Bundle extras = new Bundle();
    extras.putInt("songIndex", songIndex);
    intent.putExtras(extras);
    startActivity(intent);
  }

  public void openSettingsActivity() {
    Intent intent = new Intent(this, SettingsActivity.class);
    Bundle extras = new Bundle();
    extras.putInt("songIndex", songIndex);
    intent.putExtras(extras);
    startActivity(intent);
  }

  public void getMusic() {
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    String fileEdit = pref.getString("FileDirectory", path) + "/";
    Log.d(TAG, "Is the File Directory?: " + fileEdit);
//    Log.d(TAG, "Is the getAbsoluteFile: " + file.getAbsoluteFile());
    File f = new File(fileEdit);

    String path = f.getAbsolutePath();
//        Environment.getExternalStorageDirectory().toString() + f.getAbsoluteFile();
    Log.d(TAG, "Path: " + path);
    File directory = new File(path);
    Log.d(TAG, "PathDirectory: " + directory.toString());
    Log.d(TAG, "PathGetNameDir:" + directory.getName());
    File[] files = directory.getAbsoluteFile().listFiles();
    Log.d(TAG, "PathFiles: " + Arrays.toString(files));
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        arrayList.add(files[i].getName());
      }
    }

  }

  // Plays the song
  public void play(View v) {
    mp.start();
    if (!isPlay) {
      v.setBackgroundResource(R.drawable.ic_pause);
      player.setOnCompletionListener(mediaPlayer -> stopPlayer());
      file = new File(String.valueOf(uri));

      startFile(file);

//      player.start();
      changeSeekbar();
    } else {
      v.setBackgroundResource(R.drawable.ic_play);
      player.pause();
    }
    isPlay = !isPlay;
  }

  private void nextSong(View view) {
    songIndex += 1;
    songIndex %= arrayList.size();

    File f = new File(arrayList.get(songIndex));
    Intent intent = new Intent(this, PlayerActivity.class);
    Bundle extras = new Bundle();
    extras.putString("title", f.getAbsoluteFile().getName());
    extras.putInt("songIndex", songIndex);
    extras.putBoolean("start", true);
    intent.putExtras(extras);
    startActivity(intent);
  }

  private void previousSong(View view) {
    songIndex -= 1;
    songIndex %= arrayList.size();

    File f = new File(arrayList.get(songIndex));
    Intent intent = new Intent(this, PlayerActivity.class);
    Bundle extras = new Bundle();
    extras.putString("title", f.getAbsoluteFile().getName());
    extras.putInt("songIndex", songIndex);
    extras.putBoolean("start", true);
    intent.putExtras(extras);
    startActivity(intent);
  }

  public void pause(View v) {
    if (player != null) {
      player.pause();
    }
  }


  private void stopPlayer() {
    if (player != null) {
      player.release();
      player = null;
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    stopPlayer();
  }

  @Override
  protected void onResume() {
    super.onResume();

  }

  @Override
  public void onBackPressed() {
    finish();
  }


}
