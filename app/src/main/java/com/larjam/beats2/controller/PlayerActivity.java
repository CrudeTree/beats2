package com.larjam.beats2.controller;

import android.Manifest.permission;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import be.tarsos.dsp.io.android.AndroidAudioPlayer;
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;

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


  private int red;
  private int blue;
  private int green;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    handler = new Handler();
    Intent intent = getIntent();
    listView = findViewById(R.id.listView);
    arrayList = new ArrayList<>();
    getMusic();
    mp = MediaPlayer.create(this, R.raw.click_sound);
    mp.setVolume(0.2f, 0.2f);
    String title = arrayList.get(0);
    mNextButton = findViewById(R.id.next_button);
    mNextButton.setOnClickListener(this::nextSong);

    if (getIntent().getStringExtra("data") != null) {
      Bundle extras = intent.getExtras();
      uri = Uri.parse(getIntent().getStringExtra("data"));
      if (extras != null && extras.getString("title") != null) {
        title = extras.getString("title");
      }
    } else {
      uri = Uri.parse("/storage/emulated/0/MySongList/10,000 pesos.mp3");
      file = new File(String.valueOf(uri));
    }

//    Log.d(LOG_TAG, "URIValue: " + String.valueOf(file));
//    Log.d(LOG_TAG, "URILength: " + file.length());
//    Log.d(LOG_TAG, "URIExist: " + file.exists());
//    Log.d(LOG_TAG, "URIExecute: " + file.canExecute());

    pitchText = findViewById(R.id.text_pitch);
    originalTempoText = findViewById(R.id.original_tempo);
    keepSettingsText = findViewById(R.id.save_settings);
    displaySongIdentifier = findViewById(R.id.display_song_name);
    displaySongIdentifier.setText(title);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    mPlayButton = findViewById(R.id.play);
    setupSignIn();
    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    Log.d(TAG, "onCreate");

    player = MediaPlayer.create(this, uri);
    mPlayButton.setOnClickListener(this::play);
//    startFile(file);

    colorPreference();

    player.setOnPreparedListener(player -> {
      seekBar.setMax(player.getDuration());
      changeSeekbar();
    });
    seekBar = findViewById(R.id.seek_bar);

    seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
        if (fromUser) {
          player.seekTo(i);
        }

        if (seekBar.getProgress() >= player.getDuration() * 0.98) {
          seekBar.setProgress(0);
          songIndex += 1;
          songIndex %= arrayList.size();
          File f = new File(arrayList.get(songIndex));
          Log.d(TAG, "NameOfFile" + f.getAbsoluteFile().getName());
          Intent intent = new Intent(PlayerActivity.this, PlayerActivity.class);
          Bundle extras = new Bundle();
          extras.putString("data", arrayList.get(songIndex));
          extras.putString("title", f.getAbsoluteFile().getName());
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

    Bundle extras;
    if ((extras = intent.getExtras()) != null) {
      if (extras.getBoolean("start")) {
        play(mPlayButton);
      }
    }
  }

  private void colorPreference() {
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    Editor editor = pref.edit();
    red = pref.getInt("red", 11);
    green = pref.getInt("green", 60);
    blue = pref.getInt("blue", 73);
    Log.d(TAG, "GREEN " + green);
    toolbarColor = findViewById(R.id.toolbar);
    appBarColor = findViewById(R.id.appbar);

    originalTempoText.setTextColor(Color.rgb(red, green, blue));
    keepSettingsText.setTextColor(Color.rgb(red, green, blue));
    displaySongIdentifier.setTextColor(Color.rgb(red, green, blue));
    pitchText.setTextColor(Color.rgb(red, green, blue));
    toolbarColor.setBackgroundColor(Color.rgb(red, green, blue));
    appBarColor.setBackgroundColor(Color.rgb(red, green, blue));
  }


  private void startFile(File file) {
    int currentFactor = 4;
    final int size = 2048;
    final int overlap = 2048 - 128;
    int samplerate = 44100;
    new AndroidFFMPEGLocator(this);
    final AudioDispatcher d = AudioDispatcherFactory
        .fromPipe(file.getAbsolutePath(), samplerate, size, overlap);
    pitchShifter = new PitchShifter(2, samplerate, size, overlap);
    pitchShifter.setPitchShiftFactor(2);
    d.addAudioProcessor(new AudioProcessor() {
      @Override
      public void processingFinished() {

      }

      @Override
      public boolean process(AudioEvent audioEvent) {
        buffer = audioEvent.getFloatBuffer();
        return true;
      }
    });

    player = MediaPlayer.create(this, uri);
    player.start();

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

    Thread t = new Thread(dispatcher);
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
//    signInService.getAccount().observe(this, (account) -> viewModel.setAccount(account));
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
    // Inflate the menu; this adds items to the action bar if it is present.
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
    Intent intent = new Intent(this, SongListActivity.class);
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
    ContentResolver contentResolver = getContentResolver();
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    Uri songUri = Media.EXTERNAL_CONTENT_URI;
    Log.d(TAG, "getMusic: " + songUri);
    Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

    String fileEdit = pref.getString("FileDirectory", path) + "/";
    File f = new File(fileEdit);

    Log.d(TAG, "ThisShouldWork" + f.getAbsoluteFile().getName());

    String path =
        Environment.getExternalStorageDirectory().toString() + "/" + f.getAbsoluteFile().getName();
    Log.d("Files", "Path: " + path);
    File directory = new File(path);
    File[] files = directory.listFiles();
    Log.d("Files", "Size: " + files.length);
    for (int i = 0; i < files.length; i++) {
      Log.d("Files", "FileName:" + files[i].getName());

      arrayList.add(files[i].getName());
      Log.d(TAG, "Get absolute value  " + f.getAbsolutePath());
    }
    System.out.println("YEAHHHHBOI: " + arrayList);
  }

  // Plays the song
  public void play(View v) {
    mp.start();
    if (!isPlay) {
      v.setBackgroundResource(R.drawable.ic_pause);
      player.setOnCompletionListener(mediaPlayer -> stopPlayer());
      player.start();
      changeSeekbar();
    } else {
      v.setBackgroundResource(R.drawable.ic_play);
      player.pause();
    }
    isPlay = !isPlay;
  }

  private void nextSong(View view) {
    mp.start();
    songIndex += 1;
    songIndex %= arrayList.size();

    File f = new File(arrayList.get(songIndex));
    System.out.println(arrayList.get(songIndex));
    Intent intent = new Intent(this, PlayerActivity.class);
    Bundle extras = new Bundle();
    extras.putString("data", arrayList.get(songIndex));
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
