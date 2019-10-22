package com.larjam.beats2.model.entity.entity;
  /*
   *      _______                       _____   _____ _____
   *     |__   __|                     |  __ \ / ____|  __ \
   *        | | __ _ _ __ ___  ___  ___| |  | | (___ | |__) |
   *        | |/ _` | '__/ __|/ _ \/ __| |  | |\___ \|  ___/
   *        | | (_| | |  \__ \ (_) \__ \ |__| |____) | |
   *        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|
   *
   * -------------------------------------------------------------
   *
   * TarsosDSP is developed by Joren Six at IPEM, University Ghent
   *
   * -------------------------------------------------------------
   *
   *  Info: http://0110.be/tag/TarsosDSP
   *  Github: https://github.com/JorenSix/TarsosDSP
   *  Releases: http://0110.be/releases/TarsosDSP/
   *
   *  TarsosDSP includes modified source code by various authors,
   *  for credits and info, see README.
   *
   */
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.util.Date;
//import java.awt.BorderLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.DataLine;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.Mixer;
//import javax.sound.sampled.TargetDataLine;
//import javax.sound.sampled.UnsupportedAudioFileException;
//import javax.swing.BoxLayout;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JSlider;
//import javax.swing.JSpinner;
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import javax.swing.border.TitledBorder;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
//
//import be.tarsos.dsp.AudioDispatcher;
//import be.tarsos.dsp.AudioEvent;
//import be.tarsos.dsp.AudioProcessor;
//import be.tarsos.dsp.GainProcessor;
//import be.tarsos.dsp.MultichannelToMono;
//import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
//import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd.Parameters;
//import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
//import be.tarsos.dsp.io.jvm.AudioPlayer;
//import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
//import be.tarsos.dsp.io.jvm.WaveformWriter;
//import be.tarsos.dsp.resample.RateTransposer;

@Entity(
    foreignKeys = {
        @ForeignKey(
            entity = User.class,
            childColumns = "user_id",
            parentColumns = "user_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Audio {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "audio_id")
  private long id;

  @ColumnInfo(name = "user_id", index = true)
  private long userId;

  @ColumnInfo(name = "song_id", index = true)
  private String songId;

  @ColumnInfo(name = "pitch_id", index = true)
  private int pitchId;

//  @NonNull
//  @ColumnInfo(index = true)
//  private Date created = new Date();
//
//  @NonNull
//  @ColumnInfo(index = true)
//  private Date updated  = new Date();

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getSongId() {
    return songId;
  }

  public void setSongId(String songId) {
    this.songId = songId;
  }

  public int getPitchId() {
    return pitchId;
  }

  public void setPitchId(int pitchId) {
    this.pitchId = pitchId;
  }

//  @NonNull
//  public Date getCreated() {
//    return created;
//  }
//
//  public void setCreated(@NonNull Date created) {
//    this.created = created;
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
