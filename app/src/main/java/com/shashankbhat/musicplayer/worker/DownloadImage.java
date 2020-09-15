package com.shashankbhat.musicplayer.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;



/**
 * Created by SHASHANK BHAT on 13-Sep-20.
 *
 *
 *             Constraints constraints = new Constraints.Builder()
 *                 .setRequiredNetworkType(NetworkType.UNMETERED)
 *                 .setRequiresCharging(true)
 *                 .setRequiresBatteryNotLow(true)
 *                 .setRequiresDeviceIdle(true)
 *                 .build();
 *
 *     WorkManager workManager = WorkManager.getInstance(context);
 *     WorkRequest workRequest = new OneTimeWorkRequest
 *             .Builder(ShowNotesWorker.class)
 *                 .setConstraints(constraints)
 *             .build();
 *
 *         workManager.enqueue(workRequest);
 *
 */

public class DownloadImage extends Worker {

    public DownloadImage(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        //Work here

        return Result.success();
    }
}
