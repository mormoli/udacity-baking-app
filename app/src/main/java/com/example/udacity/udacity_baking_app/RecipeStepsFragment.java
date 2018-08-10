package com.example.udacity.udacity_baking_app;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udacity.udacity_baking_app.model.TheSteps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.squareup.picasso.Picasso;

/**
 * Fragment for playing video source of recipes making steps ..
 * using as videoplayer exoplayer
 * @see " * @see "https://codelabs.developers.google.com/codelabs/exoplayer-intro/#0"
 * */
public class RecipeStepsFragment extends Fragment {
    //bandwidth meter to measure and estimate bandwidth
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = RecipeStepsFragment.class.getSimpleName();
    private TheSteps step;
    //private TheRecipe recipe;
    //private int selectedIndex;

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private ComponentListener componentListener;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private boolean hasVideoUrl = false;

    private boolean mTwoPane;
    //private FrameLayout frameLayout;

    ImageView imageView;
    TextView textView;

    public RecipeStepsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            step = getArguments().getParcelable("Step");
            //recipe = getArguments().getParcelable("Recipe");
            //selectedIndex = getArguments().getInt("index");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps_layout, container, false);
        if (savedInstanceState != null){
            step = savedInstanceState.getParcelable("step");
        }
        //initializing attaching variables to source
        componentListener = new ComponentListener();
        playerView = rootView.findViewById(R.id.video_view);
        imageView = rootView.findViewById(R.id.thumbnail_image_view);
        textView = rootView.findViewById(R.id.steps_description_tv);
        mTwoPane = rootView.findViewById(R.id.step_details_fragment) != null;
        //frameLayout = rootView.findViewById(R.id.video_view_layout);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //checking video url if its null setting view visibility to hide in layout
        if(step.getVideoURL() == null || step.getVideoURL().isEmpty()){
            playerView.setVisibility(View.GONE);
        } else {
            hasVideoUrl = true;
        }
        if(step.getThumbnailURL() != null){  // setting thumbnail image of videos if is not null
            Uri uri = Uri.parse(step.getThumbnailURL()).buildUpon().build();
            //determine whether uri is image type or not
            //ContentResolver contentResolver = getActivity().getContentResolver();
            String type = null; //= contentResolver.getType(uri);
            String ext = MimeTypeMap.getFileExtensionFromUrl(step.getThumbnailURL());
            if(ext != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
                Log.d(TAG, "mime type of uri: " + type);
            }
            if(type != null && type.startsWith("image")) {
                Picasso.get()
                        .load(uri)
                        .into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }

        //setting description about step in to text view
        textView.setText(step.getDescription());
        /*if (mTwoPane && hasVideoUrl){//if its tablet layout and video view is visible
            //removing margin top attribute because on tablet layout we don't need tab layouts..
            FrameLayout frameLayout = view.findViewById(R.id.video_view_layout);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(frameLayout.getLayoutParams());
            params.gravity = Gravity.NO_GRAVITY;
            frameLayout.setLayoutParams(params);
            //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(frameLayout.getLayoutParams());
            //params. = 0;//.setMargins(0, 0, 0, 0);
            //frameLayout.setLayoutParams(params);
            //frameLayout.updateViewLayout(view, params);
        }*/
    }

    public void setCurrentStep(TheSteps step){
        this.step = step;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("step", step);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //ActionBar actionBar = getActivity().getActionBar();
        if(!mTwoPane) { //if this is not tablet layout call orientation change
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //if(actionBar.isShowing()) actionBar.hide();
                hideSystemUi();
            } else {
                /*FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(frameLayout.getLayoutParams());
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = 600;
                frameLayout.setLayoutParams(params);*/
                //if (!actionBar.isShowing()) actionBar.show();
                showSystemUI();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //make sure video url exist to initialize video player for.
        if(Util.SDK_INT > 23 && hasVideoUrl) initializePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if((Util.SDK_INT < 23 || player == null) && hasVideoUrl) initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23 && hasVideoUrl) releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23 && hasVideoUrl) releasePlayer();
    }
    @SuppressWarnings("ConstantConditions")
    //exoplayer initialization
    private void initializePlayer(){
        if(player == null){
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            //Using a DefaultTrackSelector with an adaptive video selection factory
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory),
                    new DefaultLoadControl()
            );
            player.addListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }
        DataSource.Factory dataSoureFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), getString(R.string.app_name)), BANDWIDTH_METER);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSoureFactory)
                .createMediaSource(Uri.parse(step.getVideoURL()));//buildMediaSource(Uri.parse(step.getVideoURL()));
        player.prepare(mediaSource, true, false);
    }

    private void releasePlayer(){
        if(player != null){
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(componentListener);
            player.release();
            player = null;
        }
    }
    /*
    private MediaSource buildMediaSource(Uri uri){
        DataSource.Factory manifestDataSourceFactory = new DefaultHttpDataSourceFactory("ua");
        DashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                new DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER));
        return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                .createMediaSource(uri);
    }*/

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(
                //View.SYSTEM_UI_FLAG_LOW_PROFILE
                View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    //@see "https://developer.android.com/training/system-ui/immersive"
    private void showSystemUI(){
        playerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private class ComponentListener extends Player.DefaultEventListener implements
            VideoRendererEventListener, AudioRendererEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case Player.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case Player.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case Player.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);
        }

        @Override
        public void onAudioEnabled(DecoderCounters counters) {

        }

        @Override
        public void onAudioSessionId(int audioSessionId) {

        }

        @Override
        public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

        }

        @Override
        public void onAudioInputFormatChanged(Format format) {

        }

        @Override
        public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

        }

        @Override
        public void onAudioDisabled(DecoderCounters counters) {

        }

        @Override
        public void onVideoEnabled(DecoderCounters counters) {

        }

        @Override
        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

        }

        @Override
        public void onVideoInputFormatChanged(Format format) {

        }

        @Override
        public void onDroppedFrames(int count, long elapsedMs) {

        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {

        }

        @Override
        public void onVideoDisabled(DecoderCounters counters) {

        }
    }
}
