package com.shashankbhat.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
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
import com.shashankbhat.musicplayer.viewmodel.SharedViewModel;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.ActivityMainBinding;
import com.shashankbhat.musicplayer.ui.song_player.SongPlayer;
import com.shashankbhat.musicplayer.utils.Player;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;
import com.shashankbhat.musicplayer.viewmodel.SharedViewModelFactory;

import static com.shashankbhat.musicplayer.utils.Constants.SONG;
import static com.shashankbhat.musicplayer.utils.Constants.SONG_ARTIST;
import static com.shashankbhat.musicplayer.utils.Constants.SONG_IMAGE_URL;
import static com.shashankbhat.musicplayer.utils.Constants.SONG_NAME;


public class MainActivity extends AppCompatActivity implements Player {

    public final int SONG_PLAYER_INTENT = 123;


    private ActivityMainBinding binding;
    private SharedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        //Receives intent when notification is clicked (Pending intent calls activity with intent)
        Song intentSong = (Song) getIntent().getSerializableExtra(SONG);

        if(intentSong==null){
            String songName = sharedPref.getString(SONG_NAME, "");
            String songArtist = sharedPref.getString(SONG_ARTIST, "");
            String songImageUrl = sharedPref.getString(SONG_IMAGE_URL, "");

            intentSong = new Song(1, songName, songArtist, 2017, "",songImageUrl);
        }

//        viewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        viewModel = ViewModelProviders.of(this, new SharedViewModelFactory(getApplication(), intentSong)).get(SharedViewModel.class);
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


    @Override
    public void playSong() {
        if (!UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
            UniqueMediaPlayer.getMediaPlayer().start();
            viewModel.isSongPlaying.setValue(true);
        }
    }

    @Override
    public void pauseSong() {
        if (UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
            UniqueMediaPlayer.getMediaPlayer().pause();
            viewModel.isSongPlaying.setValue(false);
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