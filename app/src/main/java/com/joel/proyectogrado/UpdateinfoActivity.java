package com.joel.proyectogrado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import include.MyToolbar;

public class UpdateinfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateinfo);
        MyToolbar.show(this,"Actualizar informacion",true);
    }
}