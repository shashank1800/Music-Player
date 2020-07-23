package com.shashankbhat.musicplayer.data;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 */
public class Song {

    private int songId;
    private String songName;
    private String songArtist;
    private int songReleased;
    private String songUrl;

    public Song(int songId, String songName, String songArtist, int songReleased, String songUrl) {
        this.songId = songId;
        this.songName = songName;
        this.songArtist = songArtist;
        this.songReleased = songReleased;
        this.songUrl = songUrl;
    }

    public int getSongId() {
        return songId;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public int getSongReleased() {
        return songReleased;
    }

    public String getSongUrl() {
        return songUrl;
    }
}
