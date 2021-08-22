package com.joel.proyectogrado.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.joel.proyectogrado.R;
import com.joel.proyectogrado.SessionFragment;

import include.MyToolbar;

public class LoginActivity2 extends AppCompatActivity {
    SessionFragment sessionFragment;
    SharedPreferences mPref;
    Bundle miBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        MyToolbar.show(this,"Login usuario",true);
        sessionFragment=new SessionFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.framecontenedor, sessionFragment).commit();
        mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        miBundle=new Bundle();
        String selectedUser= mPref.getString("User","");
        miBundle.putString("Rol",selectedUser);
        sessionFragment.setArguments(miBundle);
        Toast.makeText(this,selectedUser , Toast.LENGTH_SHORT).show();
    }
}