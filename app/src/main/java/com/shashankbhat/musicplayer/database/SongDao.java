package com.shashankbhat.musicplayer.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.shashankbhat.musicplayer.data.Song;

/**
 * Created by SHASHANK BHAT on 28-Jul-20.
 */

@Dao
interface SongDao {

    @Insert
    void insert(Song song);

    @Update
    void update(Song song);

    @Update
    void delete(Song song);

    @Query(value = "SELECT * FROM SONG_TABLE ORDER BY songName")
    DataSource.Factory<Integer, Song> getAllSongs();

    @Query(value = "SELECT * FROM SONG_TABLE WHERE isDownloaded=1 ORDER BY songName")
    DataSource.Factory<Integer, Song> getDownloadSongs();

}
