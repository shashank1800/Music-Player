package com.shashankbhat.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
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
import com.shashankbhat.musicplayer.databinding.ActivityMainBinding;
import com.shashankbhat.musicplayer.ui.song_player.SongPlayer;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.io.Serializable;
import java.util.Locale;

import static com.shashankbhat.musicplayer.utils.Constants.SONG;


public class MainActivity extends AppCompatActivity{
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

        String language = sharedPreferences.getString("language", "en");
        boolean mode = sharedPreferences.getBoolean("dark_mode", false);

        Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        assert language != null;
        conf.setLocale(new Locale(language));
        res.updateConfiguration(conf, dm);


        if(!mode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, key) -> {

            if(key.equals("language")){
                String lang = sharedPreferences.getString("language", "english");

            }
            if(key.equals("dark_mode")){
                boolean mod = sharedPreferences.getBoolean("dark_mode", false);
                if(!mod)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

        });

    }

    private void initPlayerClickListener() {
        binding.songLayout.setOnClickListener(view ->{

            Intent intent = new Intent(getApplicationContext(), SongPlayer.class);

            Pair<View, String> pair1 = Pair.create(binding.songName, getResources().getString(R.string.song_name));
            Pair<View, String> pair2 = Pair.create(binding.songArtist, getResources().getString(R.string.song_artist));
            Pair<View, String> pair3 = Pair.create(binding.songIcon, getResources().getString(R.string.song_icon));
            Pair<View, String> pair4 = Pair.create(binding.play, getResources().getString(R.string.song_play_button));
            Pair<View, String> pair5 = Pair.create(binding.pause, getResources().getString(R.string.song_pause_button));

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1, pair2, pair3,pair4,pair5);

            intent.putExtra(SONG, viewModel.getCurrSong().getValue());
            startActivity(intent, options.toBundle());
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

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES)
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        else
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
//        return true;
//    }

}