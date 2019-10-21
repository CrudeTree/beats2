package com.larjam.beats2.model.entity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.larjam.beats2.model.entity.entity.User;
import java.util.Collection;
import java.util.List;

@Dao
public interface UserDao {

  @Insert
  List<Long> insert(Collection<User> user_id);

  @Query("SELECT * FROM User WHERE retainOriginalTempo_id=:retainTempoId ORDER BY user_id ASC")
  User getTempoSet(int retainTempoId);

  @Query("SELECT * FROM User WHERE retainPitch_id=:retainPitchId ORDER BY user_id ASC")
  User getPitchSet(int retainPitchId);

  @Query("SELECT * FROM User WHERE pitch_id=:pitchId ORDER BY user_id ASC")
  User getPitch(int pitchId);



  @Update
  int update(User audio_id);

}
