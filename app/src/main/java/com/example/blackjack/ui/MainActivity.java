package com.example.blackjack.ui;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.blackjack.R;
import com.example.blackjack.ui.view_models.GamerViewModel;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private final int[] trackIds = {R.raw.track1, R.raw.track2, R.raw.track3};

    private GamerViewModel gamerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gamerViewModel = new ViewModelProvider(this).get(GamerViewModel.class);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> playRandomTrack());

        playRandomTrack();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mediaPlayer.pause();

        if (gamerViewModel.isFirstLaunch) gamerViewModel.insertGamer();
        else gamerViewModel.updateGamer();
        System.out.println(gamerViewModel.isFirstLaunch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaPlayer.start();
    }

    private void playRandomTrack() {
        Random random = new Random();
        int trackIndex = random.nextInt(trackIds.length);
        int trackId = trackIds[trackIndex];

        try {
            mediaPlayer.reset();
            Resources resources = getResources();
            String packageName = getPackageName();
            String trackName = resources.getResourceEntryName(trackId);
            @SuppressLint("DiscouragedApi") int resourceId = resources.getIdentifier(trackName, "raw", packageName);

            // Create a Uri object from the resource ID
            Uri trackUri = Uri.parse("android.resource://" + packageName + "/" + resourceId);

            mediaPlayer.setDataSource(getApplicationContext(), trackUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
