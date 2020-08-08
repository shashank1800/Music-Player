package com.shashankbhat.musicplayer.adapters;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.shashankbhat.musicplayer.R;

/**
 * Created by SHASHANK BHAT on 08-Aug-20.
 */
public class DataBindingAdapters {

    @BindingAdapter("app:songImage")
    public static void songImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(view);
    }

}
