package com.larjam.beats2.model.entity.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import com.larjam.beats2.model.entity.dao.AudioDao;
import com.larjam.beats2.model.entity.dao.UserDao;
import com.larjam.beats2.model.entity.entity.Audio;
import com.larjam.beats2.model.entity.entity.User;

@Database(
    entities = {Audio.class, User.class},
    version = 1, exportSchema = true
)
@TypeConverters(Beats2Database.Converters.class)
public abstract class Beats2Database extends RoomDatabase {

  protected Beats2Database() {}

  private static Application applicationContext;

  public static void setApplicationContext(Application applicationContext) {
    Beats2Database.applicationContext = applicationContext;
  }

  public static Beats2Database getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public abstract AudioDao getAudioDao();

  public abstract UserDao getUserDao();

  private static class InstanceHolder {

    private static final Beats2Database INSTANCE;

    static {
      INSTANCE =
          Room.databaseBuilder(applicationContext, Beats2Database.class, "Beats2_db").build();
    }

  }

  public static class Converters {

    @TypeConverter
    public Boolean retainTempoToBoolean(Boolean tempoRetain) {
      return tempoRetain;
    }

  }

}

















