package com.larjam.beats2.model.entity.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;


@Entity()
public class User {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "user_id")
  private long id;

  @ColumnInfo(name = "retain_original_tempo_id", index = true)
  private boolean retainOriginalTempoId;

  @ColumnInfo(name = "retain_pitch_id", index = true)
  private boolean retainPitchId;

  @ColumnInfo(name = "user_pitch_id", index = true)
  private int pitchId;

//  @NonNull
//  @ColumnInfo(index = true)
//  private Date create  = new Date();
//
//  @NonNull
//  @ColumnInfo(index = true)
//  private Date updated  = new Date();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isRetainOriginalTempoId() {
    return retainOriginalTempoId;
  }

  public void setRetainOriginalTempoId(boolean retainOriginalTempoId) {
    this.retainOriginalTempoId = retainOriginalTempoId;
  }

  public boolean isRetainPitchId() {
    return retainPitchId;
  }

  public void setRetainPitchId(boolean retainPitchId) {
    this.retainPitchId = retainPitchId;
  }

  public int getPitchId() {
    return pitchId;
  }

  public void setPitchId(int pitchId) {
    this.pitchId = pitchId;
  }

//  @NonNull
//  public Date getCreate() {
//    return create;
//  }
//
//  public void setCreate(@NonNull Date create) {
//    this.create = create;
//  }
//
//  @NonNull
//  public Date getUpdated() {
//    return updated;
//  }
//
//  public void setUpdated(@NonNull Date updated) {
//    this.updated = updated;
//  }
}
