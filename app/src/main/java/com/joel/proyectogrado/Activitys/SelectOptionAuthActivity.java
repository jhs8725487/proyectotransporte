package com.joel.proyectogrado.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.joel.proyectogrado.R;
import com.joel.proyectogrado.SessionFragment;
import com.joel.proyectogrado.client.RegisterActivity;
import com.joel.proyectogrado.drive.Register2;

import include.MyToolbar;

public class SelectOptionAuthActivity extends AppCompatActivity {
    Button mButtonGoToLogin;
    Button mButtonGoToRegister;
    SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option_auth);

        mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        MyToolbar.show(this,"Seleccionar opcion",true);

        mButtonGoToLogin=findViewById(R.id.btnGoToLogin);
        mButtonGoToRegister=findViewById(R.id.btnGoToRegister);
        mButtonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
        mButtonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }
    public void goToLogin(){
        SharedPreferences.Editor editor=mPref.edit();
        String selectedUser=mPref.getString("User","");
        if (selectedUser.equals("Cliente")) {
            editor.putString("User", "Cliente");
            editor.apply();
        }else{
            editor.putString("User","Conductor");
            editor.apply();
        }
        Intent intent = new Intent(SelectOptionAuthActivity.this, LoginActivity2.class);
        startActivity(intent);
    }
    public void goToRegister(){
        SharedPreferences.Editor editor=mPref.edit();
        String selectedUser= mPref.getString("User","");
        if (selectedUser.equals("Cliente")) {
            //Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterActivity.class);
            editor.putString("User", "Cliente");
            editor.apply();
            Intent intent =new Intent(SelectOptionAuthActivity.this,RegisterActivity.class);
            startActivity(intent);
        }else{
            editor.putString("User","Conductor");
            editor.apply();
            Intent intent = new Intent(SelectOptionAuthActivity.this, Register2.class);
            startActivity(intent);
        }
    }
}