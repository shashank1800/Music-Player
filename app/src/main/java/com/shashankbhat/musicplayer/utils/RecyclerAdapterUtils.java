package com.shashankbhat.musicplayer.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.shashankbhat.musicplayer.data.Song;

/**
 * Created by SHASHANK BHAT on 09-Aug-20.
 */
public class RecyclerAdapterUtils {

    public static final DiffUtil.ItemCallback<Song> diffCallback = new DiffUtil.ItemCallback<Song>() {
        @Override
        public boolean areItemsTheSame(
                @NonNull Song oldUser, @NonNull Song newUser) {
            return oldUser.getSongId() == newUser.getSongId();
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull Song oldUser, @NonNull Song newUser) {
            return oldUser.equals(newUser);
        }
    };
}
