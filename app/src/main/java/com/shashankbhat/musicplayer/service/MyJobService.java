package com.shashankbhat.musicplayer.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

/**
 * Created by SHASHANK BHAT on 13-Sep-20.
 */

/**
 * Manifest file
 * --------------
 * <service
 *    android:name=".MyJobService"
 *    android:permission="android.permission.BIND_JOB_SERVICE"/>
 *
 * Calling in activity
 * -------------------
 *    ComponentName componentName = new ComponentName(context, MyJobService.class);
 *     JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
 *             .setRequiresCharging(true)
 *             .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
 *             .build();
 *
 *     JobScheduler jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
 *
 *     int resultCode = jobScheduler.schedule(jobInfo);
 *
 *         if (resultCode == JobScheduler.RESULT_SUCCESS) {
 *         Log.d(TAG, "Job scheduled!");
 *     } else {
 *         Log.d(TAG, "Job not scheduled");
 *     }
 */



public class MyJobService extends JobService {

    private static final String TAG = MyJobService.class.getSimpleName();
    boolean isWorking = false;
    boolean jobCancelled = false;

    public MyJobService(){}

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("Service", "onStartJob - MyJobService");

        isWorking = true;
        // We need 'jobParameters' so we can call 'jobFinished'
        startWorkOnNewThread(jobParameters); // Services do NOT run on a separate thread

        return isWorking;
    }

    private void startWorkOnNewThread(final JobParameters jobParameters) {
        new Thread(new Runnable() {
            public void run() {
                doWork(jobParameters);
            }
        }).start();
    }

    private void doWork(JobParameters jobParameters) {
        // 10 seconds of working (1000*10ms)
        for (int i = 0; i < 1000; i++) {
            // If the job has been cancelled, stop working; the job will be rescheduled.
            if (jobCancelled)
                return;

            try {
                Thread.sleep(10);
            } catch (Exception ignored) { }
        }

        Log.d(TAG, "Job finished!");
        isWorking = false;
        jobFinished(jobParameters, false);
    }

    // Called if the job was cancelled before being finished
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before being completed.");

        jobCancelled = true;
        boolean needsReschedule = isWorking;
        jobFinished(jobParameters, needsReschedule);
        return needsReschedule;
    }


}
