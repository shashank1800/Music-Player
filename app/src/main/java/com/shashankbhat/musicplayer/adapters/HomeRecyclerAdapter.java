package com.shashankbhat.musicplayer.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.SharedViewModel;
import com.shashankbhat.musicplayer.callback.DownloadCallBack;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.data.SongDownload;
import com.shashankbhat.musicplayer.databinding.LayoutSongViewBinding;
import com.shashankbhat.musicplayer.task.DownloadSongTask;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;
import java.io.IOException;

import static com.shashankbhat.musicplayer.utils.RecyclerAdapterUtils.diffCallback;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 */

public class HomeRecyclerAdapter extends PagedListAdapter<Song, HomeRecyclerAdapter.SongViewHolder> {

    private SharedViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private Context context;

    public HomeRecyclerAdapter(SharedViewModel viewModel) {
        super(diffCallback);
        this.viewModel = viewModel;
        mediaPlayer = UniqueMediaPlayer.getMediaPlayer();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LayoutSongViewBinding binding;
        public SongViewHolder(@NonNull LayoutSongViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Song song) {
            binding.setSong(song);

            binding.downloadStatus.setOnClickListener(view -> {
                if(!song.isDownloaded()) downloadSong(song);
            });

            binding.linearLayout.setOnClickListener(this);

            song.songDownload = new SongDownload(binding.downloadStatus,binding.downloadProgress);
            song.setDownloadSettings(song.isDownloaded(), song.isDownloading(), song.getProgress());
        }

        @Override
        public void onClick(View v) {

            Song song = getItem(getAdapterPosition());

            viewModel.isSongLayoutVisible.setValue(true);
            viewModel.isSongPlaying.setValue(true);
            viewModel.setCurrSong(song);

            mediaPlayer.setOnCompletionListener(MediaPlayer::start);

            if(song != null) {
                if (song.isDownloaded())
                    playOfflineSong(song);
                else
                    playOnlineSong(song);
            }

            viewModel.setCurrSong(song);
        }
    }

    public void downloadSong(Song song) {

        song.setDownloadSettings(false,true,0);

        new DownloadSongTask(song, new DownloadCallBack() {
            @Override
            public void onCompleteListener(String path) {
                song.setDownloadSettings(true,false,100);
                song.setSongPath(path);
                viewModel.update(song);
            }

            @Override
            public void onProgressUpdate(int progress) {
                song.setDownloadSettings(false,true, progress);
            }
        }).execute();

    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LayoutSongViewBinding binding = LayoutSongViewBinding.inflate(layoutInflater);
        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = getItem(position);
        if (song != null)
            holder.bindTo(song);
    }

    private void playOfflineSong(Song song) {

        try {
            String path = song.getSongPath();

            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException ignored) { }
    }

    private void playOnlineSong(Song song) {

        viewModel.isDownloadLoaderVisible.setValue(true);

        try {
            String url = song.getSongUrl();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                viewModel.isDownloadLoaderVisible.setValue(false);
            });
            mediaPlayer.prepareAsync();

        } catch (IOException ignored) { }
    }

}
