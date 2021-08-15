package com.joel.proyectogrado.drive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.joel.proyectogrado.Activitys.MainActivity;
import com.joel.proyectogrado.Activitys.UpdateinfoActivity;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.client.AuthProvider;
import com.joel.proyectogrado.client.MapClientActivity;

import include.MyToolbar;

public class MapDriverActivity extends AppCompatActivity {
    private AuthProvider mAuthProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver);
        MyToolbar.show(this,"Conductor",true);
        mAuthProvider=new AuthProvider();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
        } else if (item.getItemId() == R.id.editar_informacion) {
            Update();
        }
        return super.onOptionsItemSelected(item);
    }
    void Update() {
        Intent intent = new Intent(MapDriverActivity.this, UpdateinfoActivity.class);
        startActivity(intent);
    }

    void logout() {
        mAuthProvider.Logout();
        Intent intent = new Intent(MapDriverActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}