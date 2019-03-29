package com.kerry.asmidterm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.MediaController;

public class MainActivity extends AppCompatActivity {

    private MediaController.MediaPlayerControl mPlayer;
    private Button mBtnDacne;
    private Button mBtnMv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnDacne = findViewById(R.id.button_dance);
        mBtnDacne.setOnClickListener(v -> {
            startActivity(new Intent(this, DancePlayerActivity.class));
        });

        mBtnMv = findViewById(R.id.button_mv);
        mBtnMv.setOnClickListener(v -> {
            startActivity(new Intent(this, MvPlayerActivity.class));
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
