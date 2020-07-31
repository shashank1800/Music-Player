package com.shashankbhat.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.ActivityMainBinding;
import com.shashankbhat.musicplayer.ui.song_player.SongPlayer;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;


import static com.shashankbhat.musicplayer.utils.Constants.SONG;


public class MainActivity extends AppCompatActivity{

    public final int SONG_PLAYER_INTENT = 123;

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

        initRequestPermission();
        initSettingsPreference();

        initBottomAppBar(binding.navView);
        initPlayerClickListener();

        initPauseClickListener();
        initPlayClickListener();
    }


    private void initSettingsPreference() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean mode = sharedPreferences.getBoolean("dark_mode", false);

//        String language = sharedPreferences.getString("language", "en");


//        Resources res = getApplicationContext().getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        android.content.res.Configuration conf = res.getConfiguration();
//        assert language != null;
//        conf.setLocale(new Locale(language));
//        res.updateConfiguration(conf, dm);


        if(!mode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, key) -> {
//
//            if(key.equals("language")){
//                String lang = sharedPreferences.getString("language", "english");
//
//            }
//            if(key.equals("dark_mode")){
//                boolean mod = sharedPreferences.getBoolean("dark_mode", false);
//                if(!mod)
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                else
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            }
//
//        });

    }

    private void initPlayerClickListener() {
        binding.songLayout.setOnClickListener(view ->{

            Intent intent = new Intent(getApplicationContext(), SongPlayer.class);

            Pair<View, String> pair1 = Pair.create(binding.songName, getResources().getString(R.string.song_name));
            Pair<View, String> pair2 = Pair.create(binding.songArtist, getResources().getString(R.string.song_artist));
            Pair<View, String> pair3 = Pair.create(binding.songIcon, getResources().getString(R.string.song_icon));

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1, pair2, pair3);

            intent.putExtra(SONG, viewModel.getCurrSong().getValue());
            startActivityForResult(intent, SONG_PLAYER_INTENT, options.toBundle());
        });

    }

    private void initRequestPermission() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                })
                .check();
    }

    private void initBottomAppBar(BottomNavigationView navView) {

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_downloads, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    private void initPauseClickListener() {

        binding.pause.setOnClickListener(v -> {
            if (UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
                UniqueMediaPlayer.getMediaPlayer().pause();
                viewModel.isSongPlaying.setValue(false);
            }
        });

    }

    private void initPlayClickListener() {

        binding.play.setOnClickListener(v -> {
            if (!UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
                UniqueMediaPlayer.getMediaPlayer().start();
                viewModel.isSongPlaying.setValue(true);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SONG_PLAYER_INTENT){
            viewModel.setCurrSong((Song) data.getSerializableExtra(SONG));
        }
    }


}