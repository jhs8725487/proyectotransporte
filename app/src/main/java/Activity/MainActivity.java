package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.joel.proyectogrado.R;

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
                editor.putString("user", "Activity/client");
                editor.apply();
            }
        });
        mButtonIAmDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSelectAuth();
                editor.putString("user", "driver");
                editor.apply();
            }
        });
    }

    private void goToSelectAuth() {
        Intent intent=new Intent(MainActivity.this, SelectOptionAuthActivity.class);
        startActivity(intent);
    }
}