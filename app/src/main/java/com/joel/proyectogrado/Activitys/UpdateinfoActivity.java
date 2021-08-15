package com.joel.proyectogrado.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.joel.proyectogrado.R;

import include.MyToolbar;

public class UpdateinfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateinfo);
        MyToolbar.show(this,"Actualizar informacion",true);
    }
}