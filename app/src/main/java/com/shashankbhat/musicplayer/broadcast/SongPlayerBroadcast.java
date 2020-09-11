package com.shashankbhat.musicplayer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

/**
 * Created by SHASHANK BHAT on 10-Sep-20.
 */
class SongPlayerBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(UniqueMediaPlayer.getMediaPlayer().isPlaying())
            UniqueMediaPlayer.getMediaPlayer().pause();
        else
            UniqueMediaPlayer.getMediaPlayer().start();
    }
}
