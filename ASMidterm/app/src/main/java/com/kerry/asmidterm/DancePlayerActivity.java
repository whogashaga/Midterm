package com.kerry.asmidterm;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout;

import com.kerry.asmidterm.controller.VideoControllerView;

import java.io.IOException;

public class DancePlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    SurfaceView videoSurface;
    MediaPlayer mPlayer;
    VideoControllerView controller;
    private static final String VIDEO_PATH = "https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/protraitVideo.mp4";
    private boolean mIsFullScreen = true;
    private boolean mIsMute = true;
    SurfaceHolder mVideoHolder;
    boolean isLand;

    //屏幕宽度
    private int mScreenWidth;
    //屏幕高度
    private int mScreenHeight;
    public static final float SHOW_SCALE = 16 * 1.0f / 9;
    private DisplayMetrics mDisplayMetrics;
    private FrameLayout mSurfaceLayout;


    /* ------------------------------------------------------------------------------------------ */
    /* Activity */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_video_player_landscape);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_video_player);
        }

        setStatusBar();

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        mVideoHolder = videoSurface.getHolder();
        mVideoHolder.addCallback(this);

        mPlayer = new MediaPlayer();
        controller = new VideoControllerView(this);

        mDisplayMetrics = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mScreenWidth = mDisplayMetrics.widthPixels;
        mScreenHeight = mDisplayMetrics.heightPixels;


        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dance));
            mPlayer.setDataSource(this, Uri.parse(VIDEO_PATH));
            mPlayer.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initView() {
        mSurfaceLayout = findViewById(R.id.videoSurfaceContainer);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceLayout.getLayoutParams();
        lp.height = (int) (mScreenWidth * SHOW_SCALE);
        mSurfaceLayout.setLayoutParams(lp);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        mVideoHolder = videoSurface.getHolder();
        mVideoHolder.addCallback(this);
    }

    private void setStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT); // calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    /* ------------------------------------------------------------------------------------------ */
    /* SurfaceHolder.Callback */

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        initView();
        mPlayer.setDisplay(holder);
        mPlayer.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mPlayer != null) {
            mPlayer.pause();
            int mCurrentPos = mPlayer.getCurrentPosition();
        }

    }

    /* ------------------------------------------------------------------------------------------ */
    /* MediaPlayer.OnPreparedListener */


    @Override
    public void onPrepared(MediaPlayer mp) {

        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoControllerContainer));
        mPlayer.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        isLand = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        mScreenWidth = mDisplayMetrics.widthPixels;
        mScreenHeight = mDisplayMetrics.heightPixels;

//        resetSize();

    }
    /* ------------------------------------------------------------------------------------------ */

    /* VideoControllerView.MediaPlayerControl */

    @Override
    public void start() {
        mPlayer.start();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        mPlayer.seekTo(pos);
    }


    @Override
    public boolean isFullScreen() {
        if (mIsFullScreen) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void toggleFullScreen() {
        setFullScreen(isFullScreen());
    }

    @Override
    public boolean isMute() {
        if (mIsMute) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void mute() {
        setVolume(isMute());
    }

    public void setVolume(boolean mute) {
        mute = false;

        if (mIsMute) {

            mPlayer.setVolume(0, 0);
            mIsMute = mute;

        } else {
            mPlayer.setVolume(1, 1);
            mIsMute = !mute;
        }
    }

    public void setFullScreen(boolean fullScreen) {
        fullScreen = false;

        if (mIsFullScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            int height = mDisplayMetrics.heightPixels;
            int width = mDisplayMetrics.widthPixels;
            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
            params.width = height;
            params.height = width;
            params.setMargins(0, 0, 0, 0);
            //set icon is full screen
            mIsFullScreen = fullScreen;

            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT); // calculateStatusColor(Color.WHITE, (int) alphaValue)
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            final FrameLayout mFrame = (FrameLayout) findViewById(R.id.videoSurfaceContainer);
//            int height = mFrame.getHeight();//get height Frame Container video
            int height = mDisplayMetrics.heightPixels;
            int width = mDisplayMetrics.widthPixels;
            android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
            params.width = width;
            params.height = height;
            params.setMargins(0, 0, 0, 0);
            //set icon is small screen
            mIsFullScreen = !fullScreen;
        }
    }
}
