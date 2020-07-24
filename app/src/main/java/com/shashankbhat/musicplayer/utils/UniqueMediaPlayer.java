package com.shashankbhat.musicplayer.utils;

import android.media.AudioAttributes;
import android.media.MediaPlayer;

/**
 * Created by SHASHANK BHAT on 24-Jul-20.
 */
public class UniqueMediaPlayer {
    private UniqueMediaPlayer(){}

    private static MediaPlayer mediaPlayer;

    public static synchronized MediaPlayer getMediaPlayer(){
        if(mediaPlayer==null){
            mediaPlayer = new MediaPlayer();

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();

            mediaPlayer.setAudioAttributes(audioAttributes);
        }
        return mediaPlayer;
    }
}
