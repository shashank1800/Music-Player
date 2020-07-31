package com.shashankbhat.musicplayer.data;

import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;
import com.shashankbhat.musicplayer.R;

import java.io.Serializable;
import java.util.Objects;

import static com.shashankbhat.musicplayer.utils.Constants.TABLE_NAME;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 */

@Entity(tableName = TABLE_NAME)
public class Song extends BaseObservable implements Serializable {

    @PrimaryKey
    @NonNull
    private int songId;

    private String songName;
    private String songArtist;
    private int songReleased;
    private String songUrl;
    private String imageUrl;
    private boolean isDownloaded;
    private String songPath;

    public Song(int songId, String songName, String songArtist, int songReleased, String songUrl, String imageUrl, boolean isDownloaded) {
        this.songId = songId;
        this.songName = songName;
        this.songArtist = songArtist;
        this.songReleased = songReleased;
        this.songUrl = songUrl;
        this.imageUrl = imageUrl;
        this.isDownloaded = isDownloaded;
        this.songPath = "";
    }

    public int getSongId() {
        return songId;
    }

    @Bindable
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

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public void setSongReleased(int songReleased) {
        this.songReleased = songReleased;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return songId == song.songId &&
                songReleased == song.songReleased &&
                songName.equals(song.songName) &&
                songArtist.equals(song.songArtist) &&
                songUrl.equals(song.songUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, songName, songArtist, songReleased, songUrl);
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", songName='" + songName + '\'' +
                ", songArtist='" + songArtist + '\'' +
                ", songReleased=" + songReleased +
                ", songUrl='" + songUrl + '\'' +
                ", isDownloaded=" + isDownloaded +
                '}';
    }
}
