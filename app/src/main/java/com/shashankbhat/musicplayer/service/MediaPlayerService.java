package com.shashankbhat.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.utils.Constants;
import com.shashankbhat.musicplayer.notification.CreateNotification;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.shashankbhat.musicplayer.utils.Constants.SONG;
import static com.shashankbhat.musicplayer.utils.Constants.SONG_NOTIFICATION_ID;

/**
 * Created by SHASHANK BHAT on 29-Aug-20.
 */
public class MediaPlayerService extends Service {

//    IBinder myBinder = new MyBinder();

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

        Log.i("Service", "onStartCommand");

        Song song = (Song) intent.getSerializableExtra(Constants.SONG);
        startForeground(SONG_NOTIFICATION_ID, CreateNotification.sendOnChannel(getApplicationContext(), song));

        return START_NOT_STICKY;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.pause();
        return super.onUnbind(intent);
    }

}
