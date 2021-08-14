package com.joel.proyectogrado.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joel.proyectogrado.R;

import include.MyToolbar;

public class RegisterActivity extends AppCompatActivity {


    //vistas
    Button mButonRegister;
    TextInputEditText mTextInputName;
    //TextInputEditText mTextInputPhone;
    //TextInputEditText mTextInputLastname;
    //RadioButton  rbtMasculino;
   // RadioButton rbtFemenino;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirmPassword;
    FirebaseAuth mAuth;
    SharedPreferences mPref;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MyToolbar.show(this,"Registro usuario",true);


        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();  //hacemos referencia al nodo principal de nuestra bd
        mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        mButonRegister=(Button) findViewById(R.id.btnGoToRegister);
        //mTextInputEmail=(TextInputEditText) findViewById(R.id.textInputEmail);
        //mTextInputPassword=(TextInputEditText)findViewById(R.id.textInputPassword);
        //mTextInputConfirmPassword=(TextInputEditText)findViewById(R.id.textInputConfirmPassword);
        //mTextInputName=(TextInputEditText)findViewById(R.id.textInputName);
       // mTextInputLastname=(TextInputEditText)findViewById(R.id.textInputLastname);
       // mTextInputPhone=(TextInputEditText)findViewById(R.id.textInputPhone);
       // rbtMasculino=(RadioButton)findViewById(R.id.rbtMasculino);
        //rbtFemenino=(RadioButton)findViewById(R.id.rbtFemenino);
        //mDialog= new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();


        mButonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(mTextInputPassword.equals(mTextInputConfirmPassword)) {
                   // ejecutarServicio("http://192.168.0.18//ejemploBDRemota/insertar_usuario.php");
                    //ejecutarServicio("http://192.168.0.11//ejemploBDRemota/insertar_usuario.php");
                    registerUser();
                //}else{
                  //  Toast.makeText(RegisterActivity.this, "Las contraseñas no coiciden", Toast.LENGTH_SHORT).show();
                //}
            }
        });
    }
    void registerUser(){
        final String Name=mTextInputName.getText().toString();
        final String Email=mTextInputEmail.getText().toString();
        final String Password=mTextInputPassword.getText().toString();
        if (!Name.isEmpty() &&  !Email.isEmpty() && !Password.isEmpty()){
            if (Password.length()>=6){
                mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           // SaveUSer(Name,Email);
                        }else{
                            Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    /*public void SaveUSer(String Name, String Email){
        String SelectedUser= mPref.getString("User","");
        if (SelectedUser.equals("Activity/Client")){
            Usuario user=new Usuario();
            user.setNombre(Name);
            user.setCorreo(Email);
            mDatabase.child("Users").child("clients").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                    }
                }
            });  //con push voy a guardar su id
        }
    }*/
   /* private void ejecutarServicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this, "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Nombre", mTextInputName.getText().toString());
                parametros.put("Apellidos", mTextInputLastname.getText().toString());
                if (rbtMasculino.isChecked()) {
                    parametros.put("Sexo", "M");
                }else{
                    parametros.put("Sexo","F");
                }
                parametros.put("Telefono", mTextInputPhone.getText().toString());
                parametros.put("Correo", mTextInputEmail.getText().toString());
                parametros.put("usu_password", mTextInputPassword.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/
}