package com.larjam.beats2.controller;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.larjam.beats2.R;

public class PlayerActivity extends AppCompatActivity {

  private boolean isPlay = false;
  private Button mPlayButton;
  private MediaPlayer player;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    mPlayButton = findViewById(R.id.play);
  }

  private View.OnClickListener mTogglePlayButton = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
    //Changes button background

      if(isPlay) {
        view.setBackgroundResource(R.drawable.ic_play);
      } else {
        view.setBackgroundResource(R.drawable.ic_pause);
      }

      isPlay = !isPlay;
    }
  };


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
    }

    return super.onOptionsItemSelected(item);
  }

  public void play(View v) {
    if (player == null) {
      player = MediaPlayer.create(this, R.raw.song);
      player.setOnCompletionListener(new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
          stopPlayer();
        }
      });
    }
    player.start();
  }

  public void pause(View v) {
    if (player != null) {
      player.pause();
    }
  }

  public void stop(View v) {
    stopPlayer();
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

}
