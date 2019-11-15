package com.larjam.beats2.controller;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Audio.Media;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProviders;
import com.larjam.beats2.GoogleSignInService;
import com.larjam.beats2.R;
import com.larjam.beats2.viewmodel.MainViewModel;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerActivity extends AppCompatActivity {

  private static final String LOG_TAG = "PlayerActivity";
  private GoogleSignInService signInService;
  private boolean isPlay = false;
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


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    handler = new Handler();
    Intent intent = getIntent();
    listView = findViewById(R.id.listView);
    arrayList = new ArrayList<>();
    getMusic();
    String title = "Pumped up Kicks - Foster The People.mp3";
    mNextButton = findViewById(R.id.next_button);
    mNextButton.setOnClickListener(this::nextSong);

    if (getIntent().getStringExtra("data") != null) {
      Bundle extras = intent.getExtras();
      uri = Uri.parse(getIntent().getStringExtra("data"));
      if (extras != null && extras.getString("title") != null) {
        title = extras.getString("title");
      }

    } else {
      uri = Uri.parse("/storage/0EBB-01CB/MySongList/Pumped up Kicks- Foster The People.mp3");

    }

    songIndex = arrayList.indexOf(uri.toString());

    displaySongIdentifier = findViewById(R.id.display_song_name);
    displaySongIdentifier.setText(title);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    mPlayButton = findViewById(R.id.play);
    setupSignIn();

    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    Log.d(LOG_TAG, "onCreate");

    player = MediaPlayer.create(this, uri);
    mPlayButton.setOnClickListener(this::play);

    seekBar = findViewById(R.id.seek_bar);
    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer player) {
        seekBar.setMax(player.getDuration());
        changeSeekbar();
      }
    });
    seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
          player.seekTo(i);
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
    if((extras = intent.getExtras())!=null){
      if (extras.getBoolean("start")){
        play(mPlayButton);
      }
    }

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
      case R.id.action_settings:
        openSongListActivity();
        break;
      case R.id.sign_out:
        signOut();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  public void openSongListActivity() {
    Intent intent = new Intent(this, SongListActivity.class);
    startActivity(intent);
  }

  public void getMusic() {
    ContentResolver contentResolver = getContentResolver();
    Uri songUri = Media.EXTERNAL_CONTENT_URI;
    Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

    if (songCursor != null && songCursor.moveToFirst()) {
      Log.d("SONGLIST", Arrays.toString(songCursor.getColumnNames()));
      do {
        arrayList.add(songCursor.getString(songCursor.getColumnIndex("_data")));
//        titleList.add(songCursor.getString(songCursor.getColumnIndex("_id")));
      } while (songCursor.moveToNext());
    }
  }

  // Plays the song
  public void play(View v) {
    if (!isPlay) {
      Log.d(LOG_TAG, String.valueOf(R.raw.song));
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
    songIndex+=1;
    songIndex %= arrayList.size();

    File f  = new File(arrayList.get(songIndex));
    System.out.println(arrayList.get(songIndex));
    Intent intent = new Intent(this, PlayerActivity.class);
    Bundle extras = new Bundle();
    extras.putString("data", arrayList.get(songIndex));
    extras.putString("title", f.getAbsoluteFile().getName());
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


  public void setSong(String path) {
    Log.d(LOG_TAG, "URI is: " + path + " : " + Uri.parse(path) + " : " + android.net.Uri
        .parse("file://" + path).getPath());
//    uri = Uri.parse(path);
  }

  @Override
  protected void onResume() {
    super.onResume();

  }
  //
//  public static PlayerActivity newInstance(Uri uri){
//    Bundle args = new Bundle();
//    if (uri != null){
//      args.putSerializable("uri", (Serializable) uri);
//    }
//    PlayerActivity playerActivity = new PlayerActivity(args);
//    return playerActivity;
//  }
}