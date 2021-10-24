package com.joel.proyectogrado.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.joel.proyectogrado.R;

public class NotificationBookingActivityActivity extends AppCompatActivity {
    Button btnAceptar;
    private MediaPlayer mMediaPlayer;
    //private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_booking_activity);
        btnAceptar=findViewById(R.id.btnAceptar);
        //mMediaPlayer =MediaPlayer.create(this, R.raw.ringtone);
        //mMediaPlayer.setLooping(true);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
        }
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizarActividad();
            }
        });
    }

    private void finalizarActividad() {
       // Intent intent=new Intent(NotificationBookingActivityActivity.this,MapClientBookingActivity.class);
        //startActivity(intent);
        //onBackPressed();
        //mMediaPlayer.pause();
        NotificationBookingActivityActivity.super.onBackPressed();
    }

  /*  @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.release();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (mMediaPlayer!=null){
            if(!mMediaPlayer.isPlaying()){
                mMediaPlayer.start();

            }

        }
    }*/

    /*@Override
    protected void onDestroy() {

        super.onDestroy();
        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
        }
    }*/
}