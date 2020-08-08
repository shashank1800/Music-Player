package com.shashankbhat.musicplayer.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.SharedViewModel;
import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.callback.DownloadCallBack;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.LayoutSongViewBinding;
import com.shashankbhat.musicplayer.task.DownloadSong;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;
import java.io.IOException;

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
            if(song.isDownloaded())
                binding.downloadStatus.setBackground(context.getDrawable(R.drawable.ic_check));
            else
                binding.downloadStatus.setBackground(context.getDrawable(R.drawable.ic_downloads));

            binding.downloadStatus.setOnClickListener(view -> {
                if(!song.isDownloaded()) downloadSong(song, binding);
            });

            binding.linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Song song = getItem(getAdapterPosition());

            viewModel.isSongLayoutVisible.setValue(true);
            viewModel.isSongPlaying.setValue(true);

            viewModel.setCurrSong(song);

            mediaPlayer.setOnCompletionListener(MediaPlayer::start);

            assert song != null;
            if(song.isDownloaded())
                playOfflineSong(song);
            else
                playOnlineSong(song);

            viewModel.setCurrSong(song);
        }
    }

    public void downloadSong(Song song, LayoutSongViewBinding binding) {

        binding.downloadProgress.setVisibility(View.VISIBLE);
        binding.downloadStatus.setVisibility(View.GONE);

        DownloadSong downloadSong = new DownloadSong(song, new DownloadCallBack() {
            @Override
            public void onCompleteListener(String path) {
                song.setDownloaded(true);
                song.setSongPath(path);
                viewModel.update(song);

                binding.downloadStatus.setVisibility(View.VISIBLE);
                binding.downloadProgress.setVisibility(View.GONE);
                binding.downloadStatus.setBackground(context.getDrawable(R.drawable.ic_check));
            }

            @Override
            public void onProgressUpdate(int progress) {
                binding.downloadProgress.setProgress(progress);
            }
        });

        downloadSong.execute();
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
