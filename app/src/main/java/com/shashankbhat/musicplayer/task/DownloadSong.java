package com.shashankbhat.musicplayer.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by SHASHANK BHAT on 27-Jul-20.
 */
public class DownloadSong extends AsyncTask<Void, Void, Void> {

    String aurl;

    public DownloadSong(String url){
        this.aurl = url;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        int count;
        try {
            URL url = new URL(aurl);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream("/song1.mp3");
            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {}
        return null;
    }

}


