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

    public MutableLiveData<Boolean>  isSongPlaying;

    public MediaPlayer mediaPlayer;

    public SongPlayerViewModel(@NonNull Application application) {
        super(application);

        mediaPlayer = UniqueMediaPlayer.getMediaPlayer();

        currentSong = new MutableLiveData<>(new Song(0,"","",0,"",false));
        isSongPlaying = new MutableLiveData<>(mediaPlayer.isPlaying());
    }

    public MutableLiveData<Song> getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong.setValue(currentSong);
    }
}
