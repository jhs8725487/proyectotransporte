package com.joel.proyectogrado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button mButtonIAmClient;
    Button mButtonIAmDriver;
    SharedPreferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        SharedPreferences.Editor editor=mPref.edit();

        mButtonIAmClient=findViewById(R.id.btnIAmClient);
        mButtonIAmDriver=findViewById(R.id.btnIAmDriver);

        mButtonIAmClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectAuth();
                editor.putString("User", "Activity/Client");
                editor.apply();
            }
        });
        mButtonIAmDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectAuth();
                editor.putString("User", "Driver");
                editor.apply();
            }
        });
    }

    private void goToSelectAuth() {
        Intent intent=new Intent(MainActivity.this, SelectOptionAuthActivity.class);
        startActivity(intent);
    }
}