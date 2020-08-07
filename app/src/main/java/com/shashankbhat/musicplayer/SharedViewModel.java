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
import com.shashankbhat.musicplayer.callback.DownloadCallBack;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.database.SongRepository;
import com.shashankbhat.musicplayer.task.DownloadSong;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.util.HashMap;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 * shashankbhat1800@gmail.com
 */
public class SharedViewModel extends AndroidViewModel {

    private MutableLiveData<Song> currentSong ;

    private LiveData<PagedList<Song>> songList, downloadedSongs;
    public MutableLiveData<HashMap<Integer,Song>> downloadingSongs;
    public MutableLiveData<HashMap<Integer, Integer>> downloadingTaskProgress;
    public MutableLiveData<Boolean> isSongLayoutVisible, isSongPlaying, isDownloadLoaderVisible;

    public MediaPlayer mediaPlayer;
    private SongRepository songRepository;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        currentSong = new MutableLiveData<>(new Song(0,"","",0,"", "",false));
        mediaPlayer =  UniqueMediaPlayer.getMediaPlayer();

        isSongLayoutVisible = new MutableLiveData<>(false);
        isDownloadLoaderVisible = new MutableLiveData<>(false);
        isSongPlaying = new MutableLiveData<>(false);

        songRepository = new SongRepository(application);

        songList = songRepository.getListOfSongs();
        downloadedSongs = songRepository.getDownloadsSong();

        downloadingSongs = new MutableLiveData<>(new HashMap<>());
        downloadingTaskProgress = new MutableLiveData<>(new HashMap<>());
    }

    public MutableLiveData<Song> getCurrSong(){
        return currentSong;
    }

    public LiveData<PagedList<Song>> getDownloadedSongs() {
        return downloadedSongs;
    }

    public void setCurrSong(Song song){
        currentSong.postValue(song);
        System.out.println(song);
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

    public void downloadSong(Song song) {

        downloadingSongs.getValue().put(song.getSongId(), song);
        downloadingTaskProgress.getValue().put(song.getSongId(), 0);

        DownloadSong downloadSong = new DownloadSong(song, new DownloadCallBack() {
            @Override
            public void onCompleteListener(Song s, String path) {
                s.setDownloaded(true);
                s.setSongPath(path);
                update(s);
            }

            @Override
            public void onProgressUpdate(Song s, int progress) {
                HashMap<Integer, Integer> task = downloadingTaskProgress.getValue();
                task.put(s.getSongId(), progress);
                downloadingTaskProgress.setValue(task);
            }
        });

        downloadSong.execute();

    }
}
