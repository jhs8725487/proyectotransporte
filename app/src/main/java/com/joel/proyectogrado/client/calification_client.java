package com.joel.proyectogrado.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joel.proyectogrado.R;

public class calification_client extends AppCompatActivity {

    private TextView mTextViewOrigin;
    private TextView mTextViewDestination;
    private RatingBar mRatingBar;
    private Button mButtonCalification;
    private float mCalification=0;
    private String mExtraOrigin;
    private String mExtraDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calification_client);
        mTextViewOrigin=findViewById(R.id.originCalification);
        mTextViewDestination=findViewById(R.id.destinationCalification);
        mRatingBar =findViewById(R.id.ratingbarCalification);
        mExtraOrigin=getIntent().getStringExtra("Origin");
        mExtraDestination=getIntent().getStringExtra("Destination");
        mButtonCalification=findViewById(R.id.btnCalificationDriver);
        mTextViewOrigin.setText(mExtraOrigin);
        mTextViewDestination.setText(mExtraDestination);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float calification, boolean b) {
                mCalification=calification;
            }
        });

        mButtonCalification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calificate();
            }
        });
    }

    private void Calificate() {
        if(mCalification>0){
            Toast.makeText(calification_client.this, "La calificacion se realizo con exito", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(calification_client.this,MapClientActivity.class);
            CommonMethod mediaplayer=new CommonMethod();
            mediaplayer.SoundPlayer(this);
            mediaplayer.player.stop();
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(calification_client.this, "Debes ingresar la calificacion", Toast.LENGTH_SHORT).show();
        }
    }
}