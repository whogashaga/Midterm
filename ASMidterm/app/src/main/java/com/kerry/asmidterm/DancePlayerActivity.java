package com.kerry.asmidterm;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;

public class DancePlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    SurfaceView videoSurface;
    MediaPlayer mPlayer;
    VideoControllerView controller;
    private static final String VIDEO_PATH = "https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/protraitVideo.mp4";


    /* ------------------------------------------------------------------------------------------ */
    /* Activity */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        mPlayer = new MediaPlayer();
        controller = new VideoControllerView(this);


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
        mPlayer.setDisplay(holder);
        mPlayer.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /* ------------------------------------------------------------------------------------------ */
    /* MediaPlayer.OnPreparedListener */

    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoControllerContainer));
        mPlayer.start();
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
        boolean isFullScreen = (getResources().getConfiguration().orientation == 2);
        Log.d("Kerry", "isFullScreen: " + isFullScreen);
        return isFullScreen;
    }

    @Override
    public void toggleFullScreen() {
        Log.d("Kerry", "toggleFullScreen");
        mPlayer.pause();
        if (getResources().getConfiguration().orientation == 1) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
