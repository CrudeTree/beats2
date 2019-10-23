package com.larjam.beats2.model.entity.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.larjam.beats2.model.entity.entity.Audio;
import java.util.Collection;
import java.util.List;

@Dao
public interface AudioDao {

  @Insert
  List<Long> insert(Collection<Audio> song_id);

  @Query("SELECT * FROM Audio WHERE audio_pitch_id=:pitchId ORDER BY audio_id ASC")
  Audio getPitch(int pitchId);

  @Update
  int update(Audio audio_id);

  @Delete
  int delete(Audio... audio);

}
