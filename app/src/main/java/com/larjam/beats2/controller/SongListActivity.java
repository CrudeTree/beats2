package com.larjam.beats2.controller;

import android.Manifest.permission;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import com.larjam.beats2.R;
import com.larjam.beats2.viewmodel.MainViewModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class SongListActivity extends FragmentActivity {

  private static final int MY_PERMISSION_REQUEST = 1;
  private ArrayList<String> arrayList;
  private ListView listView;
  private ArrayAdapter<String> adapter;
  private MainViewModel viewModel;
  private int songIndex;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);

    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    if (ContextCompat.checkSelfPermission(SongListActivity.this,
        permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(SongListActivity.this,
          permission.READ_EXTERNAL_STORAGE)) {
        ActivityCompat.requestPermissions(SongListActivity.this,
            new String[]{permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
      } else {
        ActivityCompat.requestPermissions(SongListActivity.this,
            new String[]{permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
      }
    } else {
      doStuff();
    }
  }

  // When you click the song in the playlist, it should set that song to play.
  public void doStuff() {
    listView = findViewById(R.id.listView);
    arrayList = new ArrayList<>();
    getMusic();
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener((adapterView, view, i, l) -> {
      PlayerActivity sendAudio = new PlayerActivity();
      File f  = new File(adapterView.getItemAtPosition(i).toString());
      Intent intent = new Intent(this, PlayerActivity.class);
      Bundle extras = new Bundle();
      extras.putString("data", adapterView.getItemAtPosition(i).toString());
      extras.putString("title", f.getAbsoluteFile().getName());
      intent.putExtras(extras);
      startActivity(intent);

    });
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

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSION_REQUEST: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          if (ContextCompat.checkSelfPermission(SongListActivity.this,
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
    Intent intent = new Intent(this, PlayerActivity.class);
    Bundle extras = intent.getExtras();
    System.out.println("EXTRAS" + extras);
    if (extras != null) {
      songIndex = extras.getInt("songIndex", songIndex);
    }
    System.out.println("SONGINDEX" + songIndex);

    intent.putExtra("songIndex", songIndex);
    startActivity(new Intent(this, PlayerActivity.class));
  }
}
