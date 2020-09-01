package com.shashankbhat.musicplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.SharedViewModel;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.LayoutSongDownloadsViewBinding;
import com.shashankbhat.musicplayer.service.MediaPlayerService;
import com.shashankbhat.musicplayer.utils.Constants;
import com.shashankbhat.musicplayer.utils.CreateNotification;


import static com.shashankbhat.musicplayer.utils.RecyclerAdapterUtils.diffCallback;

/**
 * Created by SHASHANK BHAT on 28-Jul-20.
 */
public class DownloadsRecyclerAdapter extends PagedListAdapter<Song, DownloadsRecyclerAdapter.SongViewHolder> {

    private SharedViewModel viewModel;

    public DownloadsRecyclerAdapter(SharedViewModel viewModel) {
        super(diffCallback);
        this.viewModel = viewModel;
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

        Intent intent = new Intent(context, MediaPlayerService.class);
        context.stopService(intent);

        intent.putExtra(Constants.SONG, song);
        context.startService(intent);

        CreateNotification.sendOnChannel(context, song, R.drawable.ic_pause, "Pause");

        viewModel.isSongLayoutVisible.setValue(true);
        viewModel.isSongPlaying.setValue(true);
    }

}
