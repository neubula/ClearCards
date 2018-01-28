package com.clearcardsapp.fragment;

import android.os.Bundle;

import com.clearcardsapp.util.Constants;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by quadri on 18/01/16.
 */
public class YouTubeVideoFragment extends YouTubePlayerSupportFragment {

    public static YouTubePlayer youTubePlayer;
    public YouTubeVideoFragment() { }

    public static YouTubeVideoFragment newInstance(String videoId) {

        YouTubeVideoFragment f = new YouTubeVideoFragment();

        Bundle b = new Bundle();
        b.putString("video_id", videoId);

        f.setArguments(b);
        f.init();

        return f;
    }

    private void init() {

        initialize(Constants.GOOGLE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;
                    youTubePlayer.cueVideo(getArguments().getString("video_id"));
//                    youTubePlayer.loadVideo(getArguments().getString("url"));
//                    youTubePlayer.play();
                }
            }

        });
    }
}
