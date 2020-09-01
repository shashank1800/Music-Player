package com.shashankbhat.musicplayer.data;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.shashankbhat.musicplayer.R;

import java.io.Serializable;
import java.util.Objects;

import static com.shashankbhat.musicplayer.utils.Constants.TABLE_NAME;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 */

@Entity(tableName = TABLE_NAME)
public class Song implements Serializable {

    @PrimaryKey
    private int songId;

    private String songName;
    private String songArtist;
    private int songReleased;
    private String songUrl;
    private String imageUrl;
    private boolean isDownloaded;
    private String songPath;

    @Ignore public SongDownload songDownload;
    @Ignore private boolean isDownloading;
    @Ignore private int progress;
//    @Ignore MyService.MyBinder myBinder;

    public Song(int songId, String songName, String songArtist, int songReleased, String songUrl, String imageUrl) {
        this.songId = songId;
        this.songName = songName;
        this.songArtist = songArtist;
        this.songReleased = songReleased;
        this.songUrl = songUrl;
        this.imageUrl = imageUrl;
        this.isDownloaded = false;
        this.songPath = "";

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

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public void setSongReleased(int songReleased) {
        this.songReleased = songReleased;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    public boolean isDownloading() {
        return isDownloading;
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

    @NonNull
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

    public void setDownloadSettings(boolean downloaded, boolean downloading, int progress) {

        setDownloaded(downloaded);
        setDownloading(downloading);
        setProgress(progress);

        Context context = songDownload.downloadStatus.getContext();

        if(downloading){
            songDownload.downloadStatus.setVisibility(View.GONE);
            songDownload.progressBar.setVisibility(View.VISIBLE);
            songDownload.progressBar.setProgress(progress);
        }
        else {
            songDownload.downloadStatus.setVisibility(View.VISIBLE);
            songDownload.progressBar.setVisibility(View.GONE);

            if(downloaded) {
                songDownload.downloadStatus.setBackground(context.getDrawable(R.drawable.ic_check));
            }else
                songDownload.downloadStatus.setBackground(context.getDrawable(R.drawable.ic_downloads));

        }
    }

//    public ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            MyService.MyBinder binder = (MyService.MyBinder)iBinder;
//            myBinder = binder;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//            myBinder = null;
//
//        }
//    };
}
