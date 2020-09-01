package com.shashankbhat.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.utils.Constants;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.io.IOException;

import static com.shashankbhat.musicplayer.service.NotificationActionService.ACTION_NAME;

/**
 * Created by SHASHANK BHAT on 29-Aug-20.
 */
public class MediaPlayerService extends Service {

//    IBinder myBinder = new MyBinder();

    public static final String DOWNLOADED = "DOWNLOADED";
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = UniqueMediaPlayer.getMediaPlayer();
    }

//    public class MyBinder extends Binder {
//        private MyService getService(){
//            return MyService.this;
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Song song = (Song) intent.getSerializableExtra(Constants.SONG);

        assert song != null;
        if (song.isDownloaded())
            playOfflineSong(song);
        else
            playOnlineSong(song);

        mediaPlayer.setOnCompletionListener(MediaPlayer::start);

        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.pause();
        return super.onUnbind(intent);
    }

    private void playOnlineSong(Song song) {

        try {
            String url = song.getSongUrl();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(mp -> {
                sendBroadcast(new Intent(getApplicationContext(), NotificationActionService.class).putExtra(ACTION_NAME, DOWNLOADED));
                mp.start();
            });
            mediaPlayer.prepareAsync();

        } catch (IOException ignored) { }
    }

    private void playOfflineSong(Song song) {

        try {
            String path = song.getSongPath();

            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException ignored) { }
    }

}
