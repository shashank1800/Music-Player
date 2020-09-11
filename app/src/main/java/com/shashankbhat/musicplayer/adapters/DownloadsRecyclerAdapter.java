package com.shashankbhat.musicplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.service.MediaPlayerService;
import com.shashankbhat.musicplayer.viewmodel.SharedViewModel;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.LayoutSongDownloadsViewBinding;
import com.shashankbhat.musicplayer.utils.Constants;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;


import java.io.IOException;

import static com.shashankbhat.musicplayer.utils.RecyclerAdapterUtils.diffCallback;

/**
 * Created by SHASHANK BHAT on 28-Jul-20.
 */
public class DownloadsRecyclerAdapter extends PagedListAdapter<Song, DownloadsRecyclerAdapter.SongViewHolder> {

    private SharedViewModel viewModel;
    private MediaPlayer mediaPlayer;

    public DownloadsRecyclerAdapter(SharedViewModel viewModel) {
        super(diffCallback);
        this.viewModel = viewModel;
        mediaPlayer = UniqueMediaPlayer.getMediaPlayer();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LayoutSongDownloadsViewBinding binding = LayoutSongDownloadsViewBinding.inflate(layoutInflater);
        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

        Song song = getItem(position);
        if (song != null)
            holder.bindTo(song);
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{

        LayoutSongDownloadsViewBinding binding;

        public SongViewHolder(@NonNull LayoutSongDownloadsViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Song song) {

            binding.setSong(song);

            binding.delete.setOnClickListener(v -> {
                song.setSongPath("");
                song.setDownloaded(false);
                viewModel.delete(song);
            });

            binding.linearLayout.setOnClickListener(v -> playAudio(binding.getRoot().getContext(), song));
        }

    }

    private void playAudio(Context context, Song song) {

        viewModel.setCurrSong(song);

        if(!song.isDownloaded())
            viewModel.isDownloadLoaderVisible.setValue(true);

        playOfflineSong(song);

        viewModel.isSongLayoutVisible.setValue(true);
        viewModel.isSongPlaying.setValue(true);

        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.putExtra(Constants.SONG, song);
        ContextCompat.startForegroundService(context, intent);

        saveInSharedPreference(context, song);
    }

    private void saveInSharedPreference(Context context, Song song) {
        SharedPreferences sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(Constants.SONG_NAME, song.getSongName());
        editor.putString(Constants.SONG_ARTIST, song.getSongArtist());
        editor.putString(Constants.SONG_IMAGE_URL, song.getImageUrl());

        editor.apply();
    }

    private void playOfflineSong(Song song) {

        try {
            String path = song.getSongPath();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException ignored) {
        }
    }

}
