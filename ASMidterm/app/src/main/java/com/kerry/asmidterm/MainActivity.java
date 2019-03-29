package com.kerry.asmidterm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;

public class MainActivity extends AppCompatActivity {

    private MediaController.MediaPlayerControl mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
