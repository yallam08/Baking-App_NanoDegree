package nano.yallam.bakingapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class StepDetailsFragment extends Fragment {
    private static final String VIDEO_URL = "mVideoURL";
    private static final String STEP_DESCRIPTION = "mStepDescription";

    private Unbinder unbinder;

    private Context mContext;

    private String mVideoURL;
    private String mStepDescription;

    private boolean mFullScreenVideo;

    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.playerView) public SimpleExoPlayerView mPlayerView;


    public StepDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();

        if (savedInstanceState != null) {
            mVideoURL = savedInstanceState.getString(VIDEO_URL);
            mStepDescription = savedInstanceState.getString(STEP_DESCRIPTION);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.play_video_icon));

        if (mVideoURL != null) {
            initializePlayer(Uri.parse(mVideoURL));
        }

        TextView stepDescriptionTV = (TextView) rootView.findViewById(R.id.tv_step_description);
        if (mStepDescription != null) {
            stepDescriptionTV.setText(mStepDescription);
        }

        //make recipe video take whole screen, preserving the scrolling
        if (mFullScreenVideo) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    height
            );
            mPlayerView.setLayoutParams(params);
        }

        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(mContext, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    mContext, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setVideoURL(String mVideoURL) {
        this.mVideoURL = mVideoURL;
    }

    public void setStepDescription(String mStepDescription) {
        this.mStepDescription = mStepDescription;
    }

    public void setFullscreenVideo(boolean fullscreenVideo) {
        this.mFullScreenVideo = fullscreenVideo;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putString(VIDEO_URL, mVideoURL);
        currentState.putString(STEP_DESCRIPTION, mStepDescription);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbinder.unbind();
    }
}
