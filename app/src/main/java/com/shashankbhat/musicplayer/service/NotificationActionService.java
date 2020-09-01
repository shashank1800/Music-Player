package com.shashankbhat.musicplayer.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.shashankbhat.musicplayer.MainActivity.BROADCAST;

/**
 * Created by SHASHANK BHAT on 01-Sep-20.
 */
public class NotificationActionService extends BroadcastReceiver {

    public static final String ACTION_NAME = "ACTION_NAME";

    @Override
    public void onReceive(Context context, Intent i) {

        Intent intent = new Intent(BROADCAST).putExtra(ACTION_NAME, i.getStringExtra(ACTION_NAME));
        context.sendBroadcast(intent);
    }
}