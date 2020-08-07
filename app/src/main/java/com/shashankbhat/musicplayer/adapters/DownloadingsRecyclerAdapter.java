package com.shashankbhat.musicplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.SharedViewModel;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.LayoutSongDownloadInProgressBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SHASHANK BHAT on 28-Jul-20.
 */
public class DownloadingsRecyclerAdapter extends RecyclerView.Adapter<DownloadingsRecyclerAdapter.SongViewHolder>{

    private SharedViewModel viewModel;
    private ArrayList<Song> songs;
    private Context context;

    public DownloadingsRecyclerAdapter(SharedViewModel viewModel) {
        this.viewModel = viewModel;
        songs = new ArrayList<>();
    }


    public class SongViewHolder extends RecyclerView.ViewHolder{

        LayoutSongDownloadInProgressBinding binding;

        public SongViewHolder(@NonNull LayoutSongDownloadInProgressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Song song) {
            binding.setSong(song);
            binding.downloadProgress.setProgress(viewModel.downloadingTaskProgress.getValue().get(song.getSongId()));
        }
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LayoutSongDownloadInProgressBinding binding = LayoutSongDownloadInProgressBinding.inflate(layoutInflater);
        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        if (song != null)
            holder.bindTo(song);
    }

    @Override
    public int getItemCount() {
        return songs!=null? songs.size() : 0;
    }


    public ArrayList<Song> getSongList() {
        return songs;
    }

    public void submitList(ArrayList<Song>  songs) {
        songs.removeAll(this.songs);
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

}
