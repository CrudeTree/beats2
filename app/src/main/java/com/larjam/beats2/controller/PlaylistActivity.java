package com.larjam.beats2.controller;

import android.Manifest.permission;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
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

public class PlaylistActivity extends FragmentActivity {

  private static final String TAG = "PlaylistActivity";
  private ArrayList<String> arrayList;
  private ListView listView;
  private ArrayAdapter<String> adapter;
  private MainViewModel viewModel;
  private int songIndex;
  private String path;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);

    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    listView = findViewById(R.id.listView);
    arrayList = new ArrayList<>();
    getMusic();
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener((adapterView, view, i, l) -> {
      File f = new File(adapterView.getItemAtPosition(i).toString());
      songIndex = i;
      Intent intent = new Intent(this, PlayerActivity.class);
      Bundle extras = new Bundle();
      extras.putString("title", f.getAbsoluteFile().getName());
      extras.putInt("songIndex", songIndex);
      intent.putExtras(extras);
      startActivity(intent);
    });
  }

  public void getMusic() {
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    String fileEdit = pref.getString("FileDirectory", path) + "/";
    File f = new File(fileEdit);
    String path =
        Environment.getExternalStorageDirectory().toString() + "/" + f.getAbsoluteFile().getName();
    File directory = new File(path);
    File[] files = directory.listFiles();
    for (int i = 0; i < files.length; i++) {
      arrayList.add(files[i].getName());
    }
  }


  @Override
  public void onBackPressed() {
    Intent intent = new Intent(this, PlayerActivity.class);
    Bundle extras = intent.getExtras();
    if (extras != null) {
      songIndex = extras.getInt("songIndex", songIndex);
    }

    intent.putExtra("songIndex", songIndex);
    startActivity(new Intent(this, PlayerActivity.class));
  }
}
