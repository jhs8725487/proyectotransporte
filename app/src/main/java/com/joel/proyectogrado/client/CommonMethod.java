package com.joel.proyectogrado.client;

import android.content.Context;
import android.media.MediaPlayer;

import com.joel.proyectogrado.R;

public class CommonMethod {
    public static MediaPlayer player;
    public static void SoundPlayer(Context ctx){
        player = MediaPlayer.create(ctx, R.raw.ringtone);
        player.setLooping(true); // Set looping
        //player.setVolume(100, 100);

        //player.release();
        player.start();
    }
}
