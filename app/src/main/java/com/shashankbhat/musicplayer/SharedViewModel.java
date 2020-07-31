package com.shashankbhat.musicplayer;

import android.app.Application;
import android.media.MediaPlayer;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.bumptech.glide.Glide;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.database.SongRepository;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 * shashankbhat1800@gmail.com
 */
public class SharedViewModel extends AndroidViewModel {

    private MutableLiveData<Song> currentSong ;

    private LiveData<PagedList<Song>> songList, downloadedSongs;

    public MutableLiveData<Boolean> isSongLayoutVisible, isSongPlaying, isDownloadLoaderVisible;

    public MediaPlayer mediaPlayer;
    private SongRepository songRepository;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        currentSong = new MutableLiveData<>(new Song(0,"","",0,"", "",false));
        mediaPlayer =  UniqueMediaPlayer.getMediaPlayer();

        isSongLayoutVisible = new MutableLiveData<>(mediaPlayer.isPlaying());
        isDownloadLoaderVisible = new MutableLiveData<>(false);
        isSongPlaying = new MutableLiveData<>(mediaPlayer.isPlaying());

        songRepository = new SongRepository(application);

        songList = songRepository.getListOfSongs();
        downloadedSongs = songRepository.getDownloadsSong();

    }

    public MutableLiveData<Song> getCurrSong(){
        return currentSong;
    }

    public LiveData<PagedList<Song>> getDownloadedSongs() {
        return downloadedSongs;
    }

    public void setCurrSong(Song song){
        currentSong.setValue(song);
        if(mediaPlayer.isPlaying())
            isSongPlaying.setValue(true);
        else
            isSongPlaying.setValue(false);

    }

    public LiveData<PagedList<Song>> getSongList() {
        return songList;
    }

    public void update(Song song){
        songRepository.update(song);
    }

    public void delete(Song song){
        songRepository.delete(song);
    }


    @BindingAdapter("app:songImage")
    public static void songImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(view);
    }

}
