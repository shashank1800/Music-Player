package com.shashankbhat.musicplayer.ui.song_player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.SeekBar;

import com.shashankbhat.musicplayer.R;
import com.shashankbhat.musicplayer.data.Song;
import com.shashankbhat.musicplayer.databinding.ActivitySongPlayerBinding;
import com.shashankbhat.musicplayer.utils.UniqueMediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

import static com.shashankbhat.musicplayer.utils.Constants.SONG;

public class SongPlayer extends AppCompatActivity {

    private ActivitySongPlayerBinding binding;
    private SongPlayerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_player);
        binding.setLifecycleOwner(this);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Song song = (Song) bundle.getSerializable(SONG);

        viewModel = ViewModelProviders.of(this).get(SongPlayerViewModel.class);

        viewModel.setCurrentSong(song);
        binding.setViewModel(viewModel);

        Intent intent = new Intent();
        intent.putExtra(SONG, viewModel.getCurrentSong().getValue());
        setResult(RESULT_OK, intent);

        initPauseClickListener();
        initPlayClickListener();
        initPlayerSeeker();
        initSongCompleteListener();
    }

    private void initPlayerSeeker() {

        final int totalMinutes = (viewModel.mediaPlayer.getDuration()/1000)/60;
        final int totalSeconds = ((viewModel.mediaPlayer.getDuration()/1000)%60);

        viewModel.setEndTime(""+totalMinutes+":"+totalSeconds);

        binding.songSeekBar.setMax(viewModel.mediaPlayer.getDuration());

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                int currentProgress = viewModel.mediaPlayer.getCurrentPosition();
                binding.songSeekBar.setProgress(currentProgress);

                int currentMinutes = (currentProgress/1000)/60;
                int currentSeconds = ((currentProgress/1000)%60);

                viewModel.setCurrentTime(""+currentMinutes+":"+currentSeconds);
            }
        },0,1000);

        binding.songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                if(b)
                {
                    viewModel.mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initPauseClickListener() {

        binding.pause.setOnClickListener(v -> {
            if (UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
                UniqueMediaPlayer.getMediaPlayer().pause();
                viewModel.setIsSongPlaying(false);
            }
        });

    }

    private void initPlayClickListener() {

        binding.play.setOnClickListener(v -> {
            if (!UniqueMediaPlayer.getMediaPlayer().isPlaying()) {
                UniqueMediaPlayer.getMediaPlayer().start();
                viewModel.setIsSongPlaying(true);
            }
        });

    }

    private void initSongCompleteListener() {
        viewModel.mediaPlayer.setOnCompletionListener(mediaPlayer -> viewModel.setIsSongPlaying(false));
    }
}