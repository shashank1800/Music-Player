package com.shashankbhat.musicplayer.callback;

import com.shashankbhat.musicplayer.data.Song;

/**
 * Created by SHASHANK BHAT on 29-Jul-20.
 */
public interface DownloadCallBack {
    void onCompleteListener(Song song, String path);
    void onProgressUpdate(Song song, int progress);
}
