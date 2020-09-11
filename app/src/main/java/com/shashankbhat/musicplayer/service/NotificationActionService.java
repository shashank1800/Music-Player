package com.shashankbhat.musicplayer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.utils.CreateNotification;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.shashankbhat.musicplayer.utils.Constants.SONG;
import static com.shashankbhat.musicplayer.utils.Constants.SONG_NOTIFICATION_ID;

/**
 * Created by SHASHANK BHAT on 01-Sep-20.
 */
public class NotificationActionService extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Song song = (Song)intent.getSerializableExtra(SONG);

        Intent songIntent = new Intent(context, MediaPlayerService.class);
        songIntent.putExtra(SONG, song);

        if(UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
            UniqueMediaPlayer.getMediaPlayer().pause();
            context.stopService(songIntent);
        }
        else {
            context.startForegroundService(songIntent);
            UniqueMediaPlayer.getMediaPlayer().start();
        }

        notificationManager.notify(SONG_NOTIFICATION_ID, CreateNotification.sendOnChannel(context, song));

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
            e.printStackTrace();
            return null;
        }
    }
}