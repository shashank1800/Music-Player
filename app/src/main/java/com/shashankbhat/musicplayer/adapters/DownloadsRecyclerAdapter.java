package com.shashankbhat.musicplayer.adapters;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.SharedViewModel;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.LayoutSongDownloadsViewBinding;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.io.IOException;

import static com.shashankbhat.musicplayer.adapters.HomeRecyclerAdapter.diffCallback;

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
        if (song != null) {
            holder.bindTo(song);
        }

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
            binding.linearLayout.setOnClickListener(v -> {
                playAudio(song);
            });
        }

    }

    private void playAudio(Song song) {

        viewModel.isSongLayoutVisible.setValue(true);
        viewModel.isSongPlaying.setValue(true);
        viewModel.setCurrSong(song);


        try {
            String path = song.getSongPath();

            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException ignored) { }
    }


}
