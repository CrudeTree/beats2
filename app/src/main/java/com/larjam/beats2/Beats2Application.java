package com.larjam.beats2;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.larjam.beats2.model.entity.service.Beats2Database;

public class Beats2Application extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);

    Beats2Database.setApplicationContext(this);
    final Beats2Database database = Beats2Database.getInstance();
    new Thread(new Runnable() {
      @Override
      public void run() {
        database.getAudioDao().delete();
      }
    }).start();
  }

}
