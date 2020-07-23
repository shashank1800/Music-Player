package com.shashankbhat.musicplayer;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.utils.FileReadHelper;

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
    private List<Song> songList = new ArrayList<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mutableSongList = new MutableLiveData<>();
        readSongsFromJSON(application.getApplicationContext());
    }

    public MutableLiveData<List<Song>> getSongsList() {
        return mutableSongList;
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

}
