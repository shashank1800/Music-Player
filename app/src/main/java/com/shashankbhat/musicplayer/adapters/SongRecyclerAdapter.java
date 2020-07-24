package com.shashankbhat.musicplayer.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.MainActivityViewModel;
import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.ActivityMainBinding;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 */
public class SongRecyclerAdapter extends RecyclerView.Adapter<SongRecyclerAdapter.SongAdapter> {

    private List<Song> songs = new ArrayList<>();
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    public SongRecyclerAdapter(ActivityMainBinding binding, MainActivityViewModel viewModel ){
        this.binding = binding;
        this.viewModel = viewModel;
    }


    public class SongAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView songName_TV, songArtist_TV, songReleased_TV;
        LinearLayout layout;

        public SongAdapter(@NonNull View itemView) {
            super(itemView);

            songName_TV = itemView.findViewById(R.id.songName);
            songArtist_TV = itemView.findViewById(R.id.songArtist);
            songReleased_TV = itemView.findViewById(R.id.songReleased);
            layout = itemView.findViewById(R.id.linear_layout);

        }

        public void bindTo(Song song){
            songName_TV.setText(song.getSongName());
            songArtist_TV.setText(song.getSongArtist());
            songReleased_TV.setText(String.valueOf(song.getSongReleased()));
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            binding.downloading.setVisibility(View.VISIBLE);

            AtomicInteger index = new AtomicInteger(getAdapterPosition());

            Song song = songs.get(index.get());
            MediaPlayer mediaPlayer = UniqueMediaPlayer.getMediaPlayer();

            viewModel.setCurrSong(song);
            mediaPlayer.setOnCompletionListener(mp -> {
                index.getAndIncrement();
                if(index.get() - songs.size()-1!=0){
                    viewModel.setCurrSong(songs.get(index.get()));
                    playAudio(songs.get(index.get()),mediaPlayer);
                }
            });
            playAudio(songs.get(index.get()),mediaPlayer);

        }
    }

    private void playAudio(Song song, MediaPlayer mediaPlayer){
        try {
            String url = song.getSongUrl();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                binding.downloading.setVisibility(View.GONE);
            });
            mediaPlayer.prepareAsync();

        } catch (IOException ignored) { }
    }

    @NonNull
    @Override
    public SongAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_song_view, parent, false);
        return new SongAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter holder, int position) {

        Song song = songs.get(position);
        if(song!=null)
            holder.bindTo(song);

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setSongs(List<Song> songs){
        this.songs = songs;
        notifyDataSetChanged();
    }

}
