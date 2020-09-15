package com.shashankbhat.musicplayer.service;

import android.app.job.JobParameters;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import static com.shashankbhat.musicplayer.utils.Constants.JOB_ID;

/**
 * Created by SHASHANK BHAT on 13-Sep-20.
 */
public class MyJobIntentService extends JobIntentService {


    /**
     * call this from on activity
     *
     * @param context application context
     * @param intent should hold context and Service class name
     */

    public static void startJobIntentService(Context context, Intent intent){
        enqueueWork(context, MyJobIntentService.class, 123, intent);

//Alternate
//enqueueWork(context, new ComponentName(context, MyJobIntentService.class), JOB_ID, intent);
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i("Service", "onHandleIntent - JobIntentService");
        return START_STICKY;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("Service", "onHandleIntent - JobIntentService");

        doWork();
    }


    private void doWork() {
        // 10 seconds of working (1000*10ms)
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                Log.i("Service", ""+i);
                Toast.makeText(MyJobIntentService.this, ""+i, Toast.LENGTH_SHORT).show();
            } catch (Exception ignored) { }
        }
    }

}
