package com.shashankbhat.musicplayer.adapters;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.MainActivityViewModel;
import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.io.IOException;

import static com.shashankbhat.musicplayer.adapters.HomeRecyclerAdapter.diffCallback;

/**
 * Created by SHASHANK BHAT on 28-Jul-20.
 */
public class DownloadsRecyclerAdapter extends PagedListAdapter<Song, DownloadsRecyclerAdapter.SongViewHolder> {

    private MainActivityViewModel viewModel;
    private MediaPlayer mediaPlayer;

    public DownloadsRecyclerAdapter(MainActivityViewModel viewModel) {
        super(diffCallback);
        this.viewModel = viewModel;
        mediaPlayer = UniqueMediaPlayer.getMediaPlayer();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_song_downloads_view, parent, false);
        return new DownloadsRecyclerAdapter.SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

        Song song = getItem(position);
        if (song != null) {
            holder.bindTo(song);
        }

    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView songName_TV, songArtist_TV, songReleased_TV;
        LinearLayout layout;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            songName_TV = itemView.findViewById(R.id.songName);
            songArtist_TV = itemView.findViewById(R.id.songArtist);
            songReleased_TV = itemView.findViewById(R.id.songReleased);
            layout = itemView.findViewById(R.id.linear_layout);
        }

        public void bindTo(Song song) {
            songName_TV.setText(song.getSongName());
            songArtist_TV.setText(song.getSongArtist());
            songReleased_TV.setText(String.valueOf(song.getSongReleased()));
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


}
