package com.shashankbhat.musicplayer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.data.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHASHANK BHAT on 23-Jul-20.
 */
public class SongRecyclerAdapter extends RecyclerView.Adapter<SongRecyclerAdapter.Adapter> {

    private List<Song> songs = new ArrayList<>();
    private Context context;

    public class Adapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView songName_TV, songArtist_TV, songReleased_TV;
        LinearLayout layout;

        public Adapter(@NonNull View itemView) {
            super(itemView);

            songName_TV = itemView.findViewById(R.id.songName);
            layout = itemView.findViewById(R.id.linear_layout);
            layout.setOnClickListener(this);
        }

        public void bindTo(Song song){
            songName_TV.setText(song.getSongName());
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_song_view, parent, false);
        return new Adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter holder, int position) {

        Song song = songs.get(position);
        if(song!=null) {
            holder.bindTo(song);
            Log.i("song not null ","binding");
        }else{
            Log.i("song null","not binding");
        }

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setSongs(List<Song> songs){
        Log.i("adapter","sets song" + songs.size());
        this.songs = songs;
        notifyDataSetChanged();
    }

}
