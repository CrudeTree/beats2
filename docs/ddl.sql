CREATE TABLE IF NOT EXISTS `Audio`
(
    `audio_id`       INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `user_id`        INTEGER                           NOT NULL,
    `song_id`        TEXT,
    `audio_pitch_id` INTEGER                           NOT NULL,
    FOREIGN KEY (`user_id`)
        REFERENCES `User` (`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX `index_Audio_user_id` ON `Audio` (`user_id`);

CREATE INDEX `index_Audio_song_id` ON `Audio` (`song_id`);

CREATE INDEX `index_Audio_audio_pitch_id` ON `Audio` (`audio_pitch_id`);

CREATE TABLE IF NOT EXISTS `User`
(
    `user_id`                  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `retain_original_tempo_id` INTEGER                           NOT NULL,
    `retain_pitch_id`          INTEGER                           NOT NULL,
    `user_pitch_id`            INTEGER                           NOT NULL
);

CREATE INDEX `index_User_retain_original_tempo_id` ON `User` (`retain_original_tempo_id`);

CREATE INDEX `index_User_retain_pitch_id` ON `User` (`retain_pitch_id`);

CREATE INDEX `index_User_user_pitch_id` ON `User` (`user_pitch_id`);
