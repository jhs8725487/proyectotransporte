package com.joel.proyectogrado.drive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.models.Driver;
import com.joel.proyectogrado.models.Usuario;
import com.joel.proyectogrado.providers.AuthProvider;
import com.joel.proyectogrado.providers.ClientProvider;
import com.joel.proyectogrado.providers.DriverProvider;

import java.util.HashMap;
import java.util.Map;

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
    TextInputEditText mTextInputCedula;
    Conductor conductor;
    Usuario usuario;
    AlertDialog mDialog;
    Bundle miBundle;
    AuthProvider mAuthProvider;
    SharedPreferences mPref;
    ClientProvider mClientProvider;
    DriverProvider mDriverProvider;
    String selectedUser;

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
        mTextInputCedula=(TextInputEditText)findViewById(R.id.textInputCedula);
        mTextInputPassword=(TextInputEditText)findViewById(R.id.textInputPassword);
        mButonRegister2=(Button) findViewById(R.id.btnGoToConfirm);
        selectedUser= mPref.getString("User","");

        mButonRegister2=(Button) findViewById(R.id.btnGoToConfirm);
        mButonRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToConfirm();
                //prueba();
            }
        });
    }
    /*void prueba(){
        Toast.makeText(this, selectedUser, Toast.LENGTH_SHORT).show();
    }*/
    public void goToConfirm(){
        Intent miIntent=new Intent(Register2.this, ConfirmPassActivity2.class);
        String selectedUser= mPref.getString("User","");
        miBundle=new Bundle();
        miBundle.putString("Nombre",mTextInputName.getText().toString());
        miBundle.putString("ApellidoPaterno",mTextInputLastname.getText().toString());
        miBundle.putString("ApellidoMaterno", mTextInputLastname2.getText().toString());
        if (rbtMasculino.isChecked()) {
            miBundle.putString("Sexo", "M");
        }else{
            miBundle.putString("Sexo","F");
        }
        miBundle.putString("Telefono",mTextInputPhone.getText().toString());
        miBundle.putString("Cedula",mTextInputCedula.getText().toString());
        miBundle.putString("Correo",mTextInputEmail.getText().toString());
        miBundle.putString("Rol",selectedUser);
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }
}