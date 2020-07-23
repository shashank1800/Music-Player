package com.shashankbhat.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.shashankbhat.musicplayer.adapters.SongRecyclerAdapter;
import com.shashankbhat.musicplayer.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        MainActivityViewModel viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        SongRecyclerAdapter adapter = new SongRecyclerAdapter();
        binding.mainActivityRv.setAdapter(adapter);
        binding.mainActivityRv.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getSongsList().observeForever(adapter::setSongs);
    }
}