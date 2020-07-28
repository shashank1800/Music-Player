package com.shashankbhat.musicplayer.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.MainActivityViewModel;
import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.io.IOException;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 */
public class HomeRecyclerAdapter extends PagedListAdapter<Song, HomeRecyclerAdapter.SongViewHolder> {

    private MainActivityViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private Context context;

    public HomeRecyclerAdapter(MainActivityViewModel viewModel) {
        super(diffCallback);
        this.viewModel = viewModel;
        mediaPlayer = UniqueMediaPlayer.getMediaPlayer();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView songName_TV, songArtist_TV, songReleased_TV;
        LinearLayout layout;
        ImageButton download_status;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            songName_TV = itemView.findViewById(R.id.songName);
            songArtist_TV = itemView.findViewById(R.id.songArtist);
            songReleased_TV = itemView.findViewById(R.id.songReleased);
            download_status = itemView.findViewById(R.id.download_status);
            layout = itemView.findViewById(R.id.linear_layout);

        }

        public void bindTo(Song song) {
            songName_TV.setText(song.getSongName());
            songArtist_TV.setText(song.getSongArtist());
            songReleased_TV.setText(String.valueOf(song.getSongReleased()));
            if(song.isDownloaded())
                download_status.setBackground(context.getDrawable(R.drawable.ic_check));
            else
                download_status.setBackground(context.getDrawable(R.drawable.ic_downloads));
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Song song = getItem(getAdapterPosition());

            viewModel.isSongLayoutVisible.setValue(true);
            viewModel.isDownloadLoaderVisible.setValue(true);
            viewModel.isSongPlaying.setValue(true);
            viewModel.setCurrSong(song);

            playAudio(song);
        }
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_song_view, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

        Song song = getItem(position);
        if (song != null) {
            holder.bindTo(song);
        }

    }

    private void playAudio(Song song) {

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
