package com.shashankbhat.musicplayer.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.shashankbhat.musicplayer.callback.DownloadCallBack;
import com.shashankbhat.musicplayer.data.Song;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by SHASHANK BHAT on 27-Jul-20.
 */
public class DownloadSongTask extends AsyncTask<Void, Void, Void> {

    private Song song;
    private boolean isDownloaded = false;
    private DownloadCallBack callBack;
    private String path;
    private long total;
    private int songSize;
    private int count=0;

    public DownloadSongTask(Song song, DownloadCallBack callBack) {
        this.song = song;
        this.callBack = callBack;

        String dir = Environment.getExternalStorageDirectory()+ "/Media Player/";

        File folder = new File(dir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        this.path = dir + song.getSongId() + "_" + song.getSongName() + ".mp3";
    }

    @Override
    protected Void doInBackground(Void... voids) {

        URLConnection connection;
        InputStream input;
        OutputStream output;

        int count;
        try {
            URL url = new URL(song.getSongUrl());
            connection = url.openConnection();
            connection.connect();
            songSize = connection.getContentLength();

            input = new BufferedInputStream(url.openStream());
            output = new FileOutputStream(path);

            byte data[] = new byte[1024];
            total=0;
            while ((count = input.read(data)) != -1) {
                total = total + count;
//                onProgressUpdate((int) (total*100/songSize));
                publishProgress();
                output.write(data, 0, count);
            }
            isDownloaded = true;

            output.flush();
            output.close();
            input.close();

        } catch (Exception e) {
            Log.i("Download", "Incomplete "+e.getMessage());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        if(count==1000){
            callBack.onProgressUpdate((int) (total*100/songSize));
            count = 0;
        }
        count++;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(isDownloaded){
            callBack.onCompleteListener(path);
            Log.i("Download", "Complete");
        }
    }

//    private void onProgressUpdate(int progress) {
//
//        if(count==1000){
//
//            count = 0;
//        }
//        count++;
//    }
}


