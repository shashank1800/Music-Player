package com.shashankbhat.musicplayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.shashankbhat.musicplayer.data.Song;

/**
 * Created by SHASHANK BHAT on 11-Sep-20.
 */
public class SharedViewModelFactory implements ViewModelProvider.Factory {

    private Song currSong;
    private Application application;

    public SharedViewModelFactory(Application application, Song currSong) {
        this.currSong = currSong;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SharedViewModel.class)){
            if(currSong==null)
                currSong = new Song(0,"","",0,"", "");
            return (T) new SharedViewModel(application, currSong);
        }
        throw new IllegalArgumentException("Unable to construct viewmodel");
    }
}
