package com.larjam.beats2.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import com.larjam.beats2.R;
import com.larjam.beats2.viewmodel.MainViewModel;
import java.io.File;
import java.util.ArrayList;

public class PlaylistActivity extends FragmentActivity {

  private static final String TAG = "PlaylistActivity";
  private ArrayList<String> arrayList;
  private ListView listView;
  private ArrayAdapter<String> adapter;
  private MainViewModel viewModel;
  private int songIndex;
  private String path;
  private Intent intent;
  private Bundle extras;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);
    intent = getIntent();
    songIndex = intent.getIntExtra("songIndex", songIndex);

    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    listView = findViewById(R.id.listView);
    arrayList = new ArrayList<>();
    getMusic();
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener((adapterView, view, i, l) -> {
      if (PlayerActivity.isPlay) {
        PlayerActivity.audioDispatcher.stop();
        PlayerActivity.isPlay = !PlayerActivity.isPlay;
      }
      songIndex = i;
      Intent intent = new Intent(this, PlayerActivity.class);
      Bundle extras = new Bundle();
      extras.putString("title", arrayList.get(i));
      extras.putInt("songIndex", songIndex);
      intent.putExtras(extras);
      startActivity(intent);
    });
  }

  public void getMusic() {
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
    String fileEdit = pref.getString("FileDirectory", path) + "/";
    File f = new File(fileEdit);
    String path = f.getAbsolutePath();
    File directory = new File(path);
    File[] files = directory.getAbsoluteFile().listFiles();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        arrayList.add(files[i].getName());
      }
    }
  }


  @Override
  public void onBackPressed() {
    Intent intent = new Intent(this, PlayerActivity.class);

    intent.putExtra("songIndex", songIndex);
    startActivity(new Intent(this, PlayerActivity.class));

  }
}
