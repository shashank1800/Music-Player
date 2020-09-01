package com.shashankbhat.musicplayer.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.service.NotificationActionService;

import static com.shashankbhat.musicplayer.application.App.CHANNEL_ID;
import static com.shashankbhat.musicplayer.service.NotificationActionService.ACTION_NAME;

/**
 * Created by SHASHANK BHAT on 01-Sep-20.
 */
public class CreateNotification {

    public static final String ACTION_PLAY = "ACTION_PLAY";

    public static void sendOnChannel(Context context, Song song, int playPauseButton, String playOrPause) {

        Intent intentPlay = new Intent(context, NotificationActionService.class).putExtra(ACTION_NAME, ACTION_PLAY);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0, intentPlay, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_play)
                .setLargeIcon(bitmap)
                .setContentTitle(song.getSongName())
                .setContentText(song.getSongArtist())
                .addAction(playPauseButton, playOrPause, pendingIntentPlay)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                .setOngoing(true)
                .build();

        notificationManager.notify(101, notification);
    }
}