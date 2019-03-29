package com.kerry.asmidterm;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;

public class DancePlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    SurfaceView videoSurface;
    MediaPlayer player;
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

        player = new MediaPlayer();
        controller = new VideoControllerView(this);


        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            player.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dance));
            player.setDataSource(this, Uri.parse(VIDEO_PATH));
            player.setOnPreparedListener(this);
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
        player.setDisplay(holder);
        player.prepareAsync();
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
        player.start();
    }

    /* ------------------------------------------------------------------------------------------ */

    /* VideoControllerView.MediaPlayerControl */

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public void pause() {
        player.pause();
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
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }


    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }
}
