package com.shashankbhat.musicplayer;

import android.app.Application;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.database.SongRepository;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 * shashankbhat1800@gmail.com
 */
public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<Song> currentSong ;
    private LiveData<PagedList<Song>> songList, downloadedSongs;

    public MutableLiveData<Boolean> isSongLayoutVisible;
    public MutableLiveData<Boolean> isDownloadLoaderVisible;
    public MediaPlayer mediaPlayer;
    public MutableLiveData<Boolean> isSongPlaying;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        currentSong = new MutableLiveData<>(new Song(0,"","",0,"", false));
        mediaPlayer =  UniqueMediaPlayer.getMediaPlayer();

        isSongLayoutVisible = new MutableLiveData<>(false);
        isDownloadLoaderVisible = new MutableLiveData<>(false);
        isSongPlaying = new MutableLiveData<>(false);

        SongRepository songRepository = new SongRepository(application);

        songList = songRepository.getListOfSongs();
        downloadedSongs = songRepository.getDownloadsSong();

    }

    public MutableLiveData<Song> getCurrSong(){
        return currentSong;
    }

    public LiveData<PagedList<Song>> getDownloadedSongs() {
        return downloadedSongs;
    }

    public void setCurrSong(Song song){ currentSong.setValue(song); }

    public LiveData<PagedList<Song>> getSongList() {
        return songList;
    }
}
