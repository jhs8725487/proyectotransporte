package com.joel.proyectogrado.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joel.proyectogrado.R;

public class NotificationBookingActivityActivity extends AppCompatActivity {
    Button btnAceptar;
    private MediaPlayer mMediaPlayer;
    String mExtraOrigin;
    String mExtraDestination;
    String mExtraTime;
    String mExtraDistance;
    TextView mtextViewRecojida;
    SharedPreferences mPref;
    TextView mtextViewTime;
    TextView mtextViewDistance;
    //private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_booking_activity);
        btnAceptar=findViewById(R.id.btnAceptar);
        mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        //mMediaPlayer =MediaPlayer.create(this, R.raw.ringtone);
        //mMediaPlayer.setLooping(true);
        mExtraOrigin=getIntent().getStringExtra("Origin");
        mMediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mMediaPlayer.setLooping(true);
       // mExtraDestination=getIntent().getStringExtra("Destination");
        //mExtraTime=getIntent().getStringExtra("Time");
        //mExtraDistance=getIntent().getStringExtra("Distance");
        mtextViewRecojida=findViewById(R.id.textViewRecojida);
        mtextViewDistance=findViewById(R.id.textviewDistance);
        mtextViewTime=findViewById(R.id.textviewTime);
        mtextViewRecojida.setText(mExtraOrigin);
        //mtextViewTime.setText(mExtraTime);
        //mtextViewDistance.setText(mExtraDistance);
        Toast.makeText(NotificationBookingActivityActivity.this, mExtraTime+" "+mExtraDistance, Toast.LENGTH_SHORT).show();
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
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