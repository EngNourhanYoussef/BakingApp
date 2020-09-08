package com.example.lap.bakingapp.UI.Steps;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lap.bakingapp.DataBase.ENTITY.StepEntity;
import com.example.lap.bakingapp.R;
import com.example.lap.bakingapp.ViewModel.StepViewModel;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class StepFragment extends Fragment {

    private static final String ARG_STEP = "arg_step";
    private static final String ARG_TWOPANE = "arg_twopane";

    private final String KEY_PLAYER_STATE = "key_player_state";
    private final String KEY_VIDEO_POSITION = "key_video_position";
    private final String KEY_WINDOW_INDEX = "key_window_index";


    private long videoPosition;
    private boolean playerState;
    private int windowIndex;

    private boolean isFullscreen = false;
    private Dialog fullScreenPlayerDialog;
    private boolean isVideo = true;
    private MediaSource videoSource;

    @BindView(R.id.player_view)
    PlayerView playerView;
    @BindView(R.id.tv_step_title)
    TextView titleTextView;
    @BindView(R.id.tv_step_description)
    TextView descriptionTextView;
    @BindView(R.id.cv_player)
    CardView playerCardView;
    @BindView(R.id.iv_thumbnail)
    ImageView thumbnailImageView;

    public StepFragment() {

    }

    public static StepFragment newInstance(long stepId, boolean isTwoPane ) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_STEP, stepId);
        args.putBoolean(ARG_TWOPANE, isTwoPane);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            long stepId = getArguments().getLong(ARG_STEP);
            boolean isTwoPane = getArguments().getBoolean(ARG_TWOPANE);
            isFullscreen = getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE && !isTwoPane;
            setupViewModel(stepId, savedInstanceState);
        }

        return view;
    }

    private void setupViewModel(long stepId, final Bundle savedInstanceState) {
        StepViewModel vm = ViewModelProviders.of(this).get(StepViewModel.class);
        vm.init(stepId);
        vm.getStep().observe(this, new Observer<StepEntity>() {
            @Override
            public void onChanged(@Nullable StepEntity stepEntity) {
                if (stepEntity != null) {
                    titleTextView.setText(stepEntity.getShortDescription());
                    descriptionTextView.setText(stepEntity.getDescription());

                    if (TextUtils.isEmpty(stepEntity.getVideoURL())) {

                        isVideo = false;
                        ((ViewGroup) playerView.getParent()).removeView(playerView);


                        if (!stepEntity.getThumbnailURL().isEmpty()) {
                            Picasso.with(StepFragment.this.getContext())
                                    .load(stepEntity.getThumbnailURL())
                                    .into(thumbnailImageView);
                        }
                    } else {
                        restoreSavedInstanceState(savedInstanceState);
                        setupMedia(stepEntity.getVideoURL());
                        initExoPlayer();
                        if (isFullscreen) {
                            openFullscreenPlayer();
                        }
                    }
                }
            }
        });
    }

    private void restoreSavedInstanceState(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            playerState = savedInstanceState.getBoolean(KEY_PLAYER_STATE);
            videoPosition = savedInstanceState.getLong(KEY_VIDEO_POSITION);
            windowIndex = savedInstanceState.getInt(KEY_WINDOW_INDEX);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void initExoPlayer() {
        if (videoSource != null && playerView.getPlayer() == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory
                    = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            playerView.setPlayer(exoPlayer);
            exoPlayer.prepare(videoSource);

            if (videoPosition != 0) {
                exoPlayer.seekTo(windowIndex, videoPosition);
            }
            exoPlayer.setPlayWhenReady(playerState);
        }
    }

    private void setupMedia(String mediaUri) {
        @SuppressWarnings("ConstantConditions")
        String userAgent = Util.getUserAgent(getContext(),
                getContext().getApplicationInfo().packageName);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        @SuppressWarnings("unchecked")
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                getContext(), userAgent, (TransferListener<? super DataSource>) bandwidthMeter);
        videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(mediaUri));
    }

    @SuppressWarnings("ConstantConditions")
    private void openFullscreenPlayer() {
        fullScreenPlayerDialog = new Dialog(getContext(),
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen) {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                //noinspection ConstantConditions
                getActivity().onBackPressed();
            }
        };

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        fullScreenPlayerDialog.addContentView(playerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        fullScreenPlayerDialog.show();
    }

    private void releasePlayer() {
        if (playerView.getPlayer() != null) {
            playerView.getPlayer().stop();
            playerView.getPlayer().release();
            playerView.setPlayer(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initExoPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isVideo) {
            if (playerView != null && playerView.getPlayer() != null) {
                playerState = playerView.getPlayer().getPlayWhenReady();
                videoPosition = playerView.getPlayer().getContentPosition();
                playerView.getPlayer().stop();
            }
            if (fullScreenPlayerDialog != null) {
                fullScreenPlayerDialog.dismiss();
            }
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (isVideo) {
            outState.putBoolean(KEY_PLAYER_STATE, playerState);
            outState.putLong(KEY_VIDEO_POSITION, videoPosition);
            outState.putInt(KEY_WINDOW_INDEX, windowIndex);
        }
        super.onSaveInstanceState(outState);
    }
}

