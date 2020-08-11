package com.shashankbhat.musicplayer.data;

import android.widget.ImageButton;

import com.dinuscxj.progressbar.CircleProgressBar;

import java.io.Serializable;

/**
 * Created by SHASHANK BHAT on 11-Aug-20.
 */
public class SongDownload implements Serializable{

    public transient  ImageButton downloadStatus;
    public transient  CircleProgressBar progressBar;

    public SongDownload(ImageButton downloadStatus, CircleProgressBar progressBar) {
        this.downloadStatus = downloadStatus;
        this.progressBar = progressBar;
    }

}