package com.shashankbhat.musicplayer.ui.song_player;

import android.app.Application;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

/**
 * Created by SHASHANK BHAT on 30-Jul-20.
 */
public class SongPlayerViewModel extends AndroidViewModel {

    private MutableLiveData<Song> currentSong ;
    private MutableLiveData<Boolean>  isSongPlaying;
    private MutableLiveData<String> currentTime, endTime;

    public MediaPlayer mediaPlayer;

    public SongPlayerViewModel(@NonNull Application application) {
        super(application);

        mediaPlayer = UniqueMediaPlayer.getMediaPlayer();
        currentTime = new MutableLiveData<>();
        endTime = new MutableLiveData<>();

        currentSong = new MutableLiveData<>(new Song(0,"","",0,"",""));
        isSongPlaying = new MutableLiveData<>(mediaPlayer.isPlaying());
    }

    public MutableLiveData<Song> getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong.setValue(currentSong);
    }

    public void setCurrentSong(MutableLiveData<Song> currentSong) {
        this.currentSong = currentSong;
    }

    public MutableLiveData<Boolean> getIsSongPlaying() {
        return isSongPlaying;
    }

    public void setIsSongPlaying(boolean isSongPlaying) {
        this.isSongPlaying.setValue(isSongPlaying);
    }

    public MutableLiveData<String> getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime.postValue(currentTime);
    }

    public MutableLiveData<String> getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime.setValue(endTime);
    }
}
