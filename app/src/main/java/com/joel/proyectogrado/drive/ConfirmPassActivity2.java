package com.joel.proyectogrado.drive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.client.RegisterActivity;
import com.joel.proyectogrado.models.Client;
import com.joel.proyectogrado.models.Driver;
import com.joel.proyectogrado.providers.AuthProvider;
import com.joel.proyectogrado.providers.ClientProvider;
import com.joel.proyectogrado.providers.DriverProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import include.MyToolbar;

public class ConfirmPassActivity2 extends AppCompatActivity {

    ImageView imagen;
    Spinner mSpinner;
    TextInputEditText mTextInputConfirmPassword;
    TextInputEditText mTextInputCedula;
    Button mButonRegister;
    Conductor conductor;
    Bundle miBundle;
    AuthProvider mAuthProvider;
    AlertDialog mDialog;
    SharedPreferences mPref;
    ClientProvider mClientProvider;
    DriverProvider mDriverProvider;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pass2);

        mAuthProvider=new AuthProvider();
        mClientProvider=new ClientProvider();
        mDriverProvider=new DriverProvider();

        mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        mDialog= new SpotsDialog.Builder().setContext(ConfirmPassActivity2.this).setMessage("Espere un momento").build();

        mTextInputConfirmPassword=(TextInputEditText) findViewById(R.id.textInputConfirmPassword);
        mTextInputCedula=(TextInputEditText) findViewById(R.id.textInputCedula);
        mButonRegister=(Button) findViewById(R.id.btnGoToRegister);
        imagen=(ImageView) findViewById(R.id.imagemId);
        MyToolbar.show(this,"Subir su fotografia",true);
        miBundle=this.getIntent().getExtras();
        String [] Ciudades={"CATEGORIA A","CATEGORIA B","CATEGORIA C"};
        mSpinner=(Spinner)findViewById(R.id.cbxcategoria);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Ciudades);
        mSpinner.setAdapter(adapter);
        //imagen=(ImageView) findViewById(R.id.imagemId);
        mButonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=miBundle.getString("Password");
                String password2=mTextInputConfirmPassword.getText().toString();
                if (password.equals(password2)) {
                   // ejecutarServicio("http://192.168.0.18//ejemploBDRemota/insertar_usuario.php");
                    //ejecutarServicio2("http://192.168.0.18//ejemploBDRemota/upload.php");
               // getStringImagen(bitmap);
                    clickRegister();
                }else{
                    Toast.makeText(ConfirmPassActivity2.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void clickRegister(){
        if (miBundle!=null) {
            final String Nombre = miBundle.getString("Nombre");
            final String Paterno = miBundle.getString("Paterno");
            final String Materno = miBundle.getString("Materno");
            final String Genero = miBundle.getString("Genero");
            final String Telefono = miBundle.getString("Telefono");
            final String Correo = miBundle.getString("Email");
            final String Contra = miBundle.getString("Password");
            final String Cedula = mTextInputCedula.getText().toString();
            final String Categoria = mSpinner.getSelectedItem().toString();
            if (!Nombre.isEmpty() && !Paterno.isEmpty() && !Telefono.isEmpty()&& !Correo.isEmpty()&& !Contra.isEmpty()&& !Cedula.isEmpty()&& !Categoria.isEmpty()) {
                if (Contra.length() >= 6) {
                    mDialog.show();
                    register(Nombre, Paterno, Materno, Genero, Telefono, Correo, Contra, Cedula, Categoria);

                } else {
                    Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
            }
        }
    }
    void register(final String Nombre,  String Paterno, String Materno, String Genero, String Telefono, final String Correo, String Contra, String Cedula,  String Categoria){
        mAuthProvider.register(Correo,Contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if(task.isSuccessful()){
                    String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Driver driver=new Driver(id,Nombre,Paterno,Materno,Genero,Telefono,Categoria,Cedula,Correo);
                    Create(driver);
                }else{
                    Toast.makeText(ConfirmPassActivity2.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void Create(Driver driver){
        mDriverProvider.create(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ConfirmPassActivity2.this, "El registro se realizo exitosamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ConfirmPassActivity2.this, "No se pudo crear el conductor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


   public void onclick(View view) {
       showFileChooser();
    }
    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Configuración del mapa de bits en ImageView
                imagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   private void ejecutarServicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            public void onResponse(String response) {
                Toast.makeText(ConfirmPassActivity2.this, "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ConfirmPassActivity2.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();

                if (miBundle!=null){
                    String nombre=miBundle.getString("Nombre");
                    String apellido=miBundle.getString("Apellidos");
                    String genero=miBundle.getString("Genero");
                    String telefono=miBundle.getString("Telefono");
                    String correo=miBundle.getString("Email");
                    String password=miBundle.getString("Password");

                    parametros.put("Nombre",nombre);
                    parametros.put("Apellidos",apellido);
                    parametros.put("Sexo",genero);
                    parametros.put("Telefono",telefono);
                    parametros.put("Correo",correo);
                    parametros.put("usu_password",password);
                }
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void ejecutarServicio2(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            public void onResponse(String response) {
                Toast.makeText(ConfirmPassActivity2.this, "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ConfirmPassActivity2.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                //Convertir bits a cadena
                String imagen=getStringImagen(bitmap);
                conductor=new Conductor(mSpinner.getSelectedItem().toString(), mTextInputCedula.getText().toString(),mTextInputConfirmPassword.getText().toString());
;                   parametros.put("Categoria",conductor.getCategoria());
                    parametros.put("Cedula",conductor.getcedula());
                    parametros.put("Imagen",imagen);
                    //parametros de retorno

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}