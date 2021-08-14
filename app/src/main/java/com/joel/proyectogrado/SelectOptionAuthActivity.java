package com.joel.proyectogrado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        Intent intent=new Intent(SelectOptionAuthActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void goToRegister(){
        String selectedUser= mPref.getString("User","");
        if (selectedUser.equals("Activity/Client")) {
            Intent intent = new Intent(SelectOptionAuthActivity.this, RegisterActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(SelectOptionAuthActivity.this, Register2.class);
            startActivity(intent);
        }
    }
}