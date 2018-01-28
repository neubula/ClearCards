package com.clearcardsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.clearcardsapp.fragment.YouTubeVideoFragment;
import com.clearcardsapp.util.AppPref;
import com.clearcardsapp.util.Constants;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    Context context;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private String videoId;
    String card_link;
    AppPref appPref;
    RelativeLayout youtubePlayerBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);
        context = this;

        appPref = new AppPref(context);

        card_link = getIntent().getStringExtra("card_link");
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(card_link);

        if (matcher.find()) {
            videoId = matcher.group();
        }

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youtubePlayerBackground = (RelativeLayout) findViewById(R.id.youtube_player_background);

        // Enabeling night view
        enableNightView();

        youTubeView.initialize(Constants.GOOGLE_API_KEY, this);

        // Enabeling night view
        enableNightView();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b && videoId != null) {
            youTubePlayer.cueVideo(videoId); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constants.GOOGLE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void enableNightView() {
        if (appPref.getBoolean("night_view")) {
            youtubePlayerBackground.setBackgroundColor(getResources().getColor(R.color.night));
        }
    }
}
