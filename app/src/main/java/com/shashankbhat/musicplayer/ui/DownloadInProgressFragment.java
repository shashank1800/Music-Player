package com.shashankbhat.musicplayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.SharedViewModel;
import com.shashankbhat.musicplayer.adapters.DownloadingsRecyclerAdapter;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.FragmentDownloadingsBinding;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.util.ArrayList;

public class DownloadInProgressFragment extends Fragment {

    private FragmentDownloadingsBinding binding;
    private SharedViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloadings, container, false);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);

        DownloadingsRecyclerAdapter adapter = new DownloadingsRecyclerAdapter(viewModel);
        binding.downloadingSongsRv.setAdapter(adapter);
        binding.downloadingSongsRv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        viewModel.getCurrSong().observeForever(song -> {
            if (UniqueMediaPlayer.getMediaPlayer().isPlaying())
                viewModel.isDownloadLoaderVisible.setValue(false);
        });

        viewModel.downloadingSongs.observe(requireActivity(), songs -> {

            ArrayList<Song> listSong = new ArrayList<>(songs.values());

            adapter.submitList(listSong);
            if(songs.size()==0)
                binding.noSongs.setVisibility(View.VISIBLE);
        });
    }


}