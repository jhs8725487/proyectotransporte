package com.joel.proyectogrado.drive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.material.textfield.TextInputEditText;
import com.joel.proyectogrado.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import include.MyToolbar;

public class ConfirmPassActivity2 extends AppCompatActivity {

    ImageView imagen;
    Spinner mSpinner;
    TextInputEditText mTextInputConfirmPassword;
    TextInputEditText mTextInputCedula;
    Button mButonRegister;
    Conductor conductor;
    Bundle miBundle;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pass2);



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
                    ejecutarServicio("http://192.168.0.18//ejemploBDRemota/insertar_usuario.php");
                    ejecutarServicio2("http://192.168.0.18//ejemploBDRemota/upload.php");
                getStringImagen(bitmap);
                }else{
                    Toast.makeText(ConfirmPassActivity2.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
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