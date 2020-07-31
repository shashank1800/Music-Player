package com.shashankbhat.musicplayer.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.shashankbhat.musicplayer.data.Song;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 */
public class FileReadHelper {

    private static String loadJSONFromAsset(@NonNull Context context, @NonNull String fileName) {
        String jsonString = null;

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            //noinspection ResultOfMethodCallIgnored
            inputStream.read(buffer);
            jsonString = new String(buffer, StandardCharsets.UTF_8);

            inputStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("File read error :" +ex.getMessage());
        }
        return jsonString;
    }


    public static List<Song> readSongsFromJSON(Context context) {

        List<Song> songList = new ArrayList<>();

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
                String imageUrl = song.getString("image_url");

                songList.add(new Song(id, songName, songArtist, songReleased, songUrl, imageUrl, false));
            }

        } catch (Exception ex) {
            System.out.println("Json parse error :" + ex.getMessage());
        }
        return songList;
    }
}
