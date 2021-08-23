package com.joel.proyectogrado.drive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
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
import com.joel.proyectogrado.models.Driver;
import com.joel.proyectogrado.providers.AuthProvider;
import com.joel.proyectogrado.providers.ClientProvider;
import com.joel.proyectogrado.providers.DriverProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import include.MyToolbar;

public class ConfirmPassActivity2 extends AppCompatActivity {
    ImageView imagen;
    Spinner mSpinner, mSpinner2;
    TextInputEditText t1;
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
    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    static final int DATE_ID = 0;
    Calendar C = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pass2);

       /* mAuthProvider=new AuthProvider();
        mClientProvider=new ClientProvider();
        mDriverProvider=new DriverProvider();*/

        mDialog= new SpotsDialog.Builder().setContext(ConfirmPassActivity2.this).setMessage("Espere un momento").build();

        sMonthIni = C.get(Calendar.MONTH);
        sDayIni = C.get(Calendar.DAY_OF_MONTH);
        sYearIni = C.get(Calendar.YEAR);

        t1=(TextInputEditText) findViewById(R.id.textInputFecha);
        mTextInputCedula=(TextInputEditText) findViewById(R.id.textInputCedula);
        mButonRegister=(Button) findViewById(R.id.btnGoToRegister);
        imagen=(ImageView) findViewById(R.id.imagemId);
        mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        MyToolbar.show(this,"Subir su fotografia",true);
        miBundle=this.getIntent().getExtras();
        String [] Categoria={"CATEGORIA A","CATEGORIA B","CATEGORIA C"};
        String [] Ciudades={"COCHABAMBA","LA PAZ","ORURO", "POTOSI","SANTA CRUZ","PANDO","TARIJA","BENI","CHUQUISACA"};
        mSpinner=(Spinner)findViewById(R.id.cbxcategoria);
        mSpinner2=(Spinner)findViewById(R.id.cbxexpedido);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Categoria);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Ciudades);
        mSpinner.setAdapter(adapter);
        mSpinner2.setAdapter(adapter2);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(DATE_ID);
            }
        });

       // imagen=(ImageView) findViewById(R.id.imagemId);
        mButonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               ejecutarServicio("http://192.168.0.15//ejemploBDRemota/insertar_usuario.php");
               ejecutarServicio2("http://192.168.0.15//ejemploBDRemota/upload.php");
                //String imagen=getStringImagen(bitmap);
                    //clickRegister();
                //prueba();
            }
        });

    }


    private void colocar_fecha() {
        t1.setText((mMonthIni + 1) + "-" + mDayIni + "-" + mYearIni+" ");
    }



    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYearIni = year;
                    mMonthIni = monthOfYear;
                    mDayIni = dayOfMonth;
                    colocar_fecha();

                }

            };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDateSetListener, sYearIni, sMonthIni, sDayIni);


        }


        return null;
    }

    /*void clickRegister(){
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
    }*/
    /*void register(final String Nombre,  String Paterno, String Materno, String Genero, String Telefono, final String Correo, String Contra, String Cedula,  String Categoria){
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
    }*/
    /*void Create(Driver driver){
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
    }*/


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
                    String Nombre=miBundle.getString("Nombre");
                    String ApellidoPaterno=miBundle.getString("ApellidoPaterno");
                    String ApellidoMaterno=miBundle.getString("ApellidoMaterno");
                    String Sexo=miBundle.getString("Sexo");
                    String Telefono=miBundle.getString("Telefono");
                    String Cedula=miBundle.getString("Cedula");
                    String Correo=miBundle.getString("Correo");
                    String Rol=miBundle.getString("Rol");

                    parametros.put("Nombre",Nombre);
                    parametros.put("ApellidoPaterno",ApellidoPaterno);
                    parametros.put("ApellidoMaterno",ApellidoMaterno);
                    parametros.put("Sexo",Sexo);
                    parametros.put("Telefono",Telefono);
                    parametros.put("Cedula",Cedula);
                    parametros.put("Correo",Correo);
                    parametros.put("Rol",Rol);
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
                conductor=new Conductor(mSpinner.getSelectedItem().toString(),mSpinner2.getSelectedItem().toString(),t1.getText().toString());
;                   parametros.put("Categoria",conductor.getCategoria());
                    parametros.put("FechaNacimiento",conductor.getFechaNacimiento());
                    parametros.put("Expedido",conductor.getExpedido());
                    parametros.put("foto",imagen);
                    //parametros de retorno

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}