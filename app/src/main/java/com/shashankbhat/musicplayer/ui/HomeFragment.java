package com.shashankbhat.musicplayer.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shashankbhat.musicplayer.SharedViewModel;
import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.adapters.HomeRecyclerAdapter;
import com.shashankbhat.musicplayer.databinding.FragmentHomeBinding;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SharedViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(binding==null) {
            binding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_home, container, false);
            binding.setLifecycleOwner(this);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        if(savedInstanceState!=null)
//            Log.i("onViewStateRestored", "onViewStateRestored: "+ savedInstanceState.getString("key"));

        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);

        HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(viewModel);
        binding.mainActivityRv.setAdapter(adapter);
        binding.mainActivityRv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        viewModel.getCurrSong().observeForever(song -> {
            if (UniqueMediaPlayer.getMediaPlayer().isPlaying())
                viewModel.isDownloadLoaderVisible.setValue(false);
        });

        viewModel.getSongList().observe(requireActivity(), adapter::submitList);
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putString("key","value");
//
//        Log.i("onViewStateRestored", "onSave");
//    }

}