package com.joel.proyectogrado.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.client.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import include.MyToolbar;

public class UpdateinfoActivity extends AppCompatActivity {
    SharedPreferences mPref;
    Button mButonActualizar;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPhone;
    TextInputEditText mTextInputLastname;
    TextInputEditText mTextInputLastname2;
    TextInputEditText mTextInputCedula;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirm;
    RadioButton rbtMasculino;
    RadioButton rbtFemenino;
    TextInputEditText mTextInputEmail;
    AlertDialog mDialog;
    RequestQueue requestQueue;
    String Mensaje;
    boolean bandera=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateinfo);
        MyToolbar.show(this,"Actualizar informacion",true);

        mButonActualizar=(Button) findViewById(R.id.btnGoToUpdate);
        mTextInputName=(TextInputEditText)findViewById(R.id.textInputName);
        mTextInputLastname=(TextInputEditText)findViewById(R.id.textInputLastName);
        mTextInputLastname2=(TextInputEditText)findViewById(R.id.textInputLastName2);
        mTextInputCedula=(TextInputEditText)findViewById(R.id.textInputCedula);
        mTextInputPhone=(TextInputEditText)findViewById(R.id.textInputPhone);
        mTextInputEmail=(TextInputEditText) findViewById(R.id.textInputEmail);
        mTextInputPassword=(TextInputEditText)findViewById(R.id.textInputPassword);
        mTextInputConfirm=(TextInputEditText)findViewById(R.id.textInputConfirmPassword);
        rbtMasculino=(RadioButton)findViewById(R.id.rbtMasculino);
        rbtFemenino=(RadioButton)findViewById(R.id.rbtFemenino);
        mPref= getSharedPreferences("typeUser", Context.MODE_PRIVATE);

        //mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        Mensaje= mPref.getString("Usuario","No existe");
        buscarUsuario("http://192.168.0.16//ejemploBDRemota/buscar_usuario.php?idUsuario=" + Mensaje + "");
        mButonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imprimir();
                //String Mensaje= mPref.getString("Usuario","No existe");
                //buscarUsuario("http://192.168.0.15//ejemploBDRemota/buscar_usuario.php");
                //buscarUsuario("http://192.168.0.15//ejemploBDRemota/buscar_usuario.php?idUsuario="+Mensaje+"");
               ejecutarServicio("http://192.168.0.16//ejemploBDRemota/editar_usuario.php");
            }
        });
    }
    /*public void imprimir(){
        String Mensaje= mPref.getString("Usuario","No existe");
        Toast.makeText(this, "Nro "+Mensaje , Toast.LENGTH_SHORT).show();
    }*/
    private void ejecutarServicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            public void onResponse(String response) {
                Toast.makeText(UpdateinfoActivity.this, "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateinfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                Mensaje= mPref.getString("Usuario","No existe");
                parametros.put("idUsuario",Mensaje);
                parametros.put("Nombre", mTextInputName.getText().toString());
                parametros.put("ApellidoPaterno", mTextInputLastname.getText().toString());
                parametros.put("ApellidoMaterno", mTextInputLastname2.getText().toString());
                if (rbtMasculino.isChecked()) {
                    parametros.put("Sexo", "M");
                }else{
                    parametros.put("Sexo","F");
                }
                parametros.put("Cedula",mTextInputCedula.getText().toString());
                parametros.put("Telefono", mTextInputPhone.getText().toString());
                parametros.put("Correo", mTextInputEmail.getText().toString());
                parametros.put("usu_password", mTextInputPassword.getText().toString());
                return parametros;
            }
        };
         requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

   private void buscarUsuario(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        mTextInputName.setText(jsonObject.getString("Nombre"));
                        mTextInputLastname.setText(jsonObject.getString("ApellidoPaterno"));
                        mTextInputLastname2.setText(jsonObject.getString("ApellidoMaterno"));
                        mTextInputPhone.setText(jsonObject.getString("Telefono"));
                        mTextInputCedula.setText(jsonObject.getString("Cedula"));
                        mTextInputEmail.setText(jsonObject.getString("Correo"));
                        mTextInputPassword.setText(jsonObject.getString("usu_password"));
                        mTextInputConfirm.setText(jsonObject.getString("usu_password"));
                    } catch (JSONException e) {
                        Toast.makeText(UpdateinfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateinfoActivity.this, "FALLO LA CONEXION", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
   }

}