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

import com.shashankbhat.musicplayer.SharedViewModel;
import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.adapters.DownloadsRecyclerAdapter;
import com.shashankbhat.musicplayer.databinding.FragmentDownloadBinding;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;


public class DownloadFragment extends Fragment {

    private FragmentDownloadBinding binding;
    private SharedViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(binding==null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false);
            binding.setLifecycleOwner(this);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);

        DownloadsRecyclerAdapter adapter = new DownloadsRecyclerAdapter(viewModel);
        binding.downloadedSongsRv.setAdapter(adapter);
        binding.downloadedSongsRv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        viewModel.getCurrSong().observeForever(song -> {
            if (UniqueMediaPlayer.getMediaPlayer().isPlaying())
                viewModel.isDownloadLoaderVisible.setValue(false);
        });

        viewModel.getDownloadedSongs().observe(requireActivity(), songs -> {
            adapter.submitList(songs);
            if(songs.size()==0)
                binding.noSongs.setVisibility(View.VISIBLE);
        });
    }


}