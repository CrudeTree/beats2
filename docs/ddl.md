## Data Definition Language (DDL) for data model

```sql
CREATE TABLE IF NOT EXISTS `Audio`
(
    `audio_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `user_id`
               INTEGER                           NOT NULL,
    `song_id`  TEXT,
    `pitch_id` INTEGER                           NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES
        `User` (`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX `index_Audio_user_id` ON `Audio` (`user_id`);

CREATE INDEX `index_Audio_song_id` ON `Audio` (`song_id`);

CREATE INDEX `index_Audio_pitch_id` ON `Audio` (`pitch_id`);

CREATE TABLE IF NOT EXISTS `User`
(
    `user_id`                INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `retainOriginalTempo_id` INTEGER                           NOT NULL,
    `retainPitch_id`         INTEGER                           NOT NULL,
    `pitch_id`               INTEGER
                                                               NOT NULL
);

CREATE INDEX `index_User_retainOriginalTempo_id` ON `User` (`retainOriginalTempo_id`);

CREATE INDEX `index_User_retainPitch_id` ON `User` (`retainPitch_id`);

CREATE INDEX `index_User_pitch_id` ON `User` (`pitch_id`);

```