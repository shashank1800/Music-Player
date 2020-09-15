package com.shashankbhat.musicplayer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.service.MediaPlayerService;
import com.shashankbhat.musicplayer.notification.CreateNotification;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;


import static com.shashankbhat.musicplayer.utils.Constants.SONG;
import static com.shashankbhat.musicplayer.utils.Constants.SONG_NOTIFICATION_ID;

/**
 * Created by SHASHANK BHAT on 01-Sep-20.
 */
public class NotificationActionReceiver extends BroadcastReceiver {
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
}