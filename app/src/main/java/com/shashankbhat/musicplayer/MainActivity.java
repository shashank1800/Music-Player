package com.shashankbhat.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shashankbhat.musicplayer.adapters.SongRecyclerAdapter;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.ActivityMainBinding;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        SongRecyclerAdapter adapter = new SongRecyclerAdapter(binding, viewModel);
        binding.mainActivityRv.setAdapter(adapter);
        binding.mainActivityRv.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getSongsList().observeForever(adapter::setSongs);
        viewModel.getCurrSong().observeForever(song ->{
            viewModel.setSongLayout(binding);
            if(UniqueMediaPlayer.getMediaPlayer().isPlaying())
                binding.downloading.setVisibility(View.GONE);
        });

        setSongLayoutVisibility();
        initPauseClickListener();
        initPlayClickListener();
    }

    private void setSongLayoutVisibility() {
        Song currSong = viewModel.getCurrSong().getValue();
        viewModel.setCurrSong(currSong);
    }

    private void initPauseClickListener() {

        binding.pause.setOnClickListener(v->{
            if(UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
                UniqueMediaPlayer.getMediaPlayer().pause();
                binding.play.setVisibility(View.VISIBLE);
                binding.pause.setVisibility(View.GONE);
            }
        });

    }

    private void initPlayClickListener() {

        binding.play.setOnClickListener(v -> {
            if(!UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
                UniqueMediaPlayer.getMediaPlayer().start();
                binding.play.setVisibility(View.GONE);
                binding.pause.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        return true;
    }

}