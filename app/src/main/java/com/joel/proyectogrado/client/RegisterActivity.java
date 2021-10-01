package com.joel.proyectogrado.client;

import static com.joel.proyectogrado.client.Constants.MY_DEFAULT_TIMEOUT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import com.joel.proyectogrado.providers.AuthProvider;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.providers.ClientProvider;
import com.joel.proyectogrado.providers.DriverProvider;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import include.MyToolbar;

public class RegisterActivity extends AppCompatActivity {


    //vistas
    Button mButonRegister;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPhone;
    TextInputEditText mTextInputLastname;
    TextInputEditText mTextInputLastname2;
    TextInputEditText mTextInputCedula;
    RadioButton rbtMasculino;
    RadioButton rbtFemenino;
    TextInputEditText mTextInputEmail;
    AlertDialog mDialog;
    AuthProvider mAuthProvider;
    SharedPreferences mPref;
    ClientProvider mClientProvider;
    DriverProvider mDriverProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MyToolbar.show(this,"Registro usuario",true);

        //mAuthProvider=new AuthProvider();
        //mClientProvider=new ClientProvider();
        //mDriverProvider=new DriverProvider();

        mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        mButonRegister=(Button) findViewById(R.id.btnGoToRegister);
        mTextInputEmail=(TextInputEditText) findViewById(R.id.textInputEmail);
        mTextInputName=(TextInputEditText)findViewById(R.id.textInputName);
        mTextInputLastname=(TextInputEditText)findViewById(R.id.textInputLastName);
        mTextInputLastname2=(TextInputEditText)findViewById(R.id.textInputLastName2);
        mTextInputCedula=(TextInputEditText)findViewById(R.id.textInputCedula);
        mTextInputPhone=(TextInputEditText)findViewById(R.id.textInputPhone);
        rbtMasculino=(RadioButton)findViewById(R.id.rbtMasculino);
        rbtFemenino=(RadioButton)findViewById(R.id.rbtFemenino);
       // mDialog= new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();


        mButonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(mTextInputPassword.equals(mTextInputConfirmPassword)) {
                    ejecutarServicio("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/insertar_usuario.php");
                   // clickRegister();
                    //prueba();
                //}else{
                  //  Toast.makeText(RegisterActivity.this, "Las contraseñas no coiciden", Toast.LENGTH_SHORT).show();
                //}
            }
        });
    }
   /* void prueba(){
        String selectedUser= mPref.getString("User","");
        Toast.makeText(this, selectedUser, Toast.LENGTH_SHORT).show();
    }*/
    /*void clickRegister(){
        final String Name=mTextInputName.getText().toString();
        final String Email=mTextInputEmail.getText().toString();
        final String Password=mTextInputPassword.getText().toString();
        if (!Name.isEmpty() &&  !Email.isEmpty() && !Password.isEmpty()){
            if (Password.length()>=6){
                mDialog.show();
                register(Name, Email,Password);

            }else{
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    void register(final String name, final String email, String Password){
        mAuthProvider.register(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if(task.isSuccessful()){
                    String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Client client=new Client(id,name,email);
                    Create(client);
                }else{
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void Create(Client client){
        mClientProvider.create(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "El registro se realizo exitosamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void SaveUSer(String id,String Name, String Email){
        String SelectedUser= mPref.getString("User","");
        if (SelectedUser.equals("Activity/Client")){
            Client user=new Client();
            user.setNombre(Name);
            user.setCorreo(Email);
            mDatabase.child("Users").child("clients").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    private void ejecutarServicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this, "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(RegisterActivity.this, "BEBA", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                String selectedUser= mPref.getString("User","");
                parametros.put("Nombre", mTextInputName.getText().toString());
                parametros.put("ApellidoPaterno", mTextInputLastname.getText().toString());
                parametros.put("ApellidoMaterno", mTextInputLastname2.getText().toString());
                if (rbtMasculino.isChecked()) {
                    parametros.put("Sexo", "M");
                }else{
                    parametros.put("Sexo","F");
                }
                parametros.put("Telefono", mTextInputPhone.getText().toString());
                parametros.put("Cedula", mTextInputCedula.getText().toString());
                parametros.put("Correo", mTextInputEmail.getText().toString());
                parametros.put("Rol",selectedUser);
                return parametros;
            }
        };
        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}