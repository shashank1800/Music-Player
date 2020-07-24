package com.shashankbhat.musicplayer;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.ActivityMainBinding;
import com.shashankbhat.musicplayer.utils.FileReadHelper;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 * shashankbhat1800@gmail.com
 */
public class MainActivityViewModel extends AndroidViewModel {

    private  MutableLiveData<List<Song>> mutableSongList;
    private MutableLiveData<Song> currentSong ;
    private MediaPlayer mediaPlayer;
    private List<Song> songList = new ArrayList<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mutableSongList = new MutableLiveData<>();
        currentSong = new MediatorLiveData<>();
        mediaPlayer  = UniqueMediaPlayer.getMediaPlayer();

        readSongsFromJSON(application.getApplicationContext());
    }

    public MutableLiveData<List<Song>> getSongsList() {
        return mutableSongList;
    }

    public MutableLiveData<Song> getCurrSong(){
        return currentSong;
    }

    public void setCurrSong(Song song){
        this.currentSong.postValue(song);
    }

    private void readSongsFromJSON(Context context) {

        String jsonString = FileReadHelper.loadJSONFromAsset(context, "songs.json");

        try {
            JSONArray songs = new JSONArray(jsonString);

            for (int index = 0; index < songs.length(); index++) {
                JSONObject song = songs.getJSONObject(index);
                System.out.println(song.toString());
                int id = song.getInt("id");
                String songName = song.getString("name");
                String songArtist = song.getString("artist");
                int songReleased = song.getInt("released-year");
                String songUrl = song.getString("url");

                songList.add(new Song(id, songName, songArtist, songReleased, songUrl));
            }

            mutableSongList.postValue(songList);

        } catch (Exception ex) {
            System.out.println("Json parse error :" + ex.getMessage());
        }

    }

    public void setSongLayout(ActivityMainBinding binding){

        Song song = getCurrSong().getValue();

        if(song!=null) {
            binding.songName.setText(song.getSongName());
            binding.songArtist.setText(song.getSongArtist());
            binding.songLayout.setVisibility(View.VISIBLE);
            binding.pause.setVisibility(View.VISIBLE);
            binding.play.setVisibility(View.GONE);
        }
    }
}
