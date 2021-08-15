package com.joel.proyectogrado.drive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.models.Driver;
import com.joel.proyectogrado.providers.AuthProvider;
import com.joel.proyectogrado.providers.ClientProvider;
import com.joel.proyectogrado.providers.DriverProvider;

import dmax.dialog.SpotsDialog;
import include.MyToolbar;

public class Register2 extends AppCompatActivity {

    Button mButonRegister2;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputLastname;
    RadioButton rbtMasculino;
    RadioButton rbtFemenino;
    TextInputEditText mTextInputPhone;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputLastname2;
    Driver driver;
    AlertDialog mDialog;
    Bundle miBundle;
    AuthProvider mAuthProvider;
    SharedPreferences mPref;
    ClientProvider mClientProvider;
    DriverProvider mDriverProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mAuthProvider=new AuthProvider();
        mClientProvider=new ClientProvider();
        mDriverProvider=new DriverProvider();

        mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        mDialog= new SpotsDialog.Builder().setContext(Register2.this).setMessage("Espere un momento").build();

        MyToolbar.show(this,"Registro conductor",true);
        mTextInputName=(TextInputEditText)findViewById(R.id.textInputName);
        mTextInputLastname=(TextInputEditText)findViewById(R.id.textInputLastname);
        mTextInputLastname2=(TextInputEditText) findViewById(R.id.textInputLastname2);
        rbtMasculino=(RadioButton)findViewById(R.id.rbtMasculino);
        rbtFemenino=(RadioButton)findViewById(R.id.rbtFemenino);
        mTextInputPhone=(TextInputEditText)findViewById(R.id.textInputPhone);
        mTextInputEmail=(TextInputEditText) findViewById(R.id.textInputEmail);
        mTextInputPassword=(TextInputEditText)findViewById(R.id.textInputPassword);
        mButonRegister2=(Button) findViewById(R.id.btnGoToConfirm);

        mButonRegister2=(Button) findViewById(R.id.btnGoToConfirm);
        mButonRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToConfirm();
            }
        });
    }
    public void goToConfirm(){
        if (rbtFemenino.isChecked()) {
            driver = new Driver(mTextInputName.getText().toString(), mTextInputLastname.getText().toString(),mTextInputLastname2.getText().toString(), "F", mTextInputPhone.getText().toString(), mTextInputEmail.getText().toString(), mTextInputPassword.getText().toString());
        }else{
            driver = new Driver(mTextInputName.getText().toString(), mTextInputLastname.getText().toString(),mTextInputLastname2.getText().toString(), "M", mTextInputPhone.getText().toString(), mTextInputEmail.getText().toString(), mTextInputPassword.getText().toString());
        }
        Intent miIntent=new Intent(Register2.this, ConfirmPassActivity2.class);
        miBundle=new Bundle();
        miBundle.putString("Nombre",driver.getNombre());
        miBundle.putString("Paterno",driver.getApellidoPaterno());
        miBundle.putString("Materno", driver.getApellidoMaterno());
        miBundle.putString("Genero",driver.getGenero());
        miBundle.putString("Telefono",driver.getTelefono());
        miBundle.putString("Email",driver.getCorreo());
        miBundle.putString("Password",driver.getContra());
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }
}