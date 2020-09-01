package com.shashankbhat.musicplayer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.shashankbhat.musicplayer.databinding.ActivityMainBinding;
import com.shashankbhat.musicplayer.ui.song_player.SongPlayer;
import com.shashankbhat.musicplayer.utils.CreateNotification;
import com.shashankbhat.musicplayer.utils.Player;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.util.Objects;
import java.util.logging.Logger;

import static com.shashankbhat.musicplayer.service.MediaPlayerService.DOWNLOADED;
import static com.shashankbhat.musicplayer.service.NotificationActionService.ACTION_NAME;
import static com.shashankbhat.musicplayer.utils.Constants.SONG;


public class MainActivity extends AppCompatActivity implements Player {

    public final int SONG_PLAYER_INTENT = 123;
    public static final String BROADCAST = "com.shashankbhat.musicplayer.BROADCAST";

    private ActivityMainBinding binding;
    private SharedViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        binding.setViewModel(viewModel);

        binding.pause.setOnClickListener(v -> pauseSong());
        binding.play.setOnClickListener(v -> playSong());

        initRequestPermission();
        initSettingsPreference();

        initBottomAppBar();
        initPlayerClickListener();
    }

    private void initPlayerClickListener() {

        binding.songLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SongPlayer.class);

            Pair<View, String> pair1 = Pair.create(binding.songName, getResources().getString(R.string.song_name));
            Pair<View, String> pair2 = Pair.create(binding.songArtist, getResources().getString(R.string.song_artist));
            Pair<View, String> pair3 = Pair.create(binding.songIcon, getResources().getString(R.string.song_icon));

            //noinspection unchecked
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1, pair2, pair3);

            intent.putExtra(SONG, viewModel.getCurrSong().getValue());
            startActivityForResult(intent, SONG_PLAYER_INTENT, options.toBundle());
        });

    }


    private void initBottomAppBar() {

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_downloads, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SONG_PLAYER_INTENT) {

            if (viewModel.mediaPlayer.isPlaying())
                viewModel.isSongPlaying.setValue(true);
            else
                viewModel.isSongPlaying.setValue(false);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = Objects.requireNonNull(intent.getExtras()).getString(ACTION_NAME);

            assert action != null;
            switch (action) {
                case CreateNotification.ACTION_PLAY:
                    if (viewModel.isSongPlaying.getValue())
                        pauseSong();
                    else
                        playSong();
                    break;
                case DOWNLOADED:
                    viewModel.isDownloadLoaderVisible.postValue(false);
                    break;
            }

        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void playSong() {
        if (!UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
            UniqueMediaPlayer.getMediaPlayer().start();
            viewModel.isSongPlaying.setValue(true);
            CreateNotification.sendOnChannel(this, viewModel.getCurrSong().getValue(), R.drawable.ic_pause, "Pause");
        }
    }

    @Override
    public void pauseSong() {
        if (UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
            UniqueMediaPlayer.getMediaPlayer().pause();
            viewModel.isSongPlaying.setValue(false);
            CreateNotification.sendOnChannel(this, viewModel.getCurrSong().getValue(), R.drawable.ic_play, "Play");
        }
    }

    private void initRequestPermission() {
        MultiplePermissionsListener snackBarMultiplePermissionsListener =
                SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                        .with(binding.constraintLayout, "App requires permission to store the downloaded song")
                        .withOpenSettingsButton("Settings")
                        .build();

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(snackBarMultiplePermissionsListener).check();
    }

    private void initSettingsPreference() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean mode = sharedPreferences.getBoolean("dark_mode", false);

        if (!mode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

    }
}