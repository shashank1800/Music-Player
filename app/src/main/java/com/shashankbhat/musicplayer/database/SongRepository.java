package com.shashankbhat.musicplayer.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.shashankbhat.musicplayer.data.Song;

/**
 * Created by SHASHANK BHAT on 28-Jul-20.
 */
public class SongRepository {

    private LiveData<PagedList<Song>> listOfSongs,downloadsSong;
    private SongDao songDao;

    public SongRepository(Application application){
        SongDatabase songDatabase = SongDatabase.getInstance(application.getApplicationContext());
        songDao = songDatabase.mSongDao();

        listOfSongs = new LivePagedListBuilder<>(this.songDao.getAllSongs(),/*Page size*/ 20).build();
        downloadsSong = new LivePagedListBuilder<>(this.songDao.getDownloadSongs(),/*Page size*/ 20).build();
    }

    public LiveData<PagedList<Song>> getListOfSongs() {
        return listOfSongs;
    }

    public LiveData<PagedList<Song>> getDownloadsSong() {
        return downloadsSong;
    }

    public void update(Song song){
        System.out.println("update"+song.getSongName());
        new UpdateTask(song).execute();
    }

    public void delete(Song song){
        new DeleteTask(song).execute();
    }

    class UpdateTask extends AsyncTask<Void, Void, Void>{
        private Song song;

        public UpdateTask(Song song) {
            this.song = song;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            songDao.update(song);
            return null;
        }
    }

    class DeleteTask extends AsyncTask<Void, Void, Void>{
        private Song song;

        public DeleteTask(Song song) {
            this.song = song;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            songDao.delete(song);
            return null;
        }
    }
}
