package com.joel.proyectogrado;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.joel.proyectogrado.client.MapClientActivity;
import com.joel.proyectogrado.drive.MapDriverActivity;
import com.joel.proyectogrado.models.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;


public class SessionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    RequestQueue rq;
    JsonRequest jrq;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;
    String selectedUser;
    SharedPreferences mPref;
    AlertDialog mDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment;

        //return inflater.inflate(R.layout.fragment_session, container, false);
        View vista=inflater.inflate(R.layout.fragment_session,container,false);
        mTextInputEmail=(TextInputEditText) vista.findViewById(R.id.textInputEmail);
        mTextInputPassword=(TextInputEditText) vista.findViewById(R.id.textInputPassword);
        mPref= getContext().getSharedPreferences("typeUser", Context.MODE_PRIVATE);
        mDialog=new SpotsDialog.Builder().setContext(getContext()).setMessage("Espere un momento").build();
        mButtonLogin=(Button) vista.findViewById(R.id.btnLogin);
        rq= Volley.newRequestQueue(getContext());
        if (getArguments()!=null){
            selectedUser=getArguments().getString("Rol","");
        }
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUser=getArguments().getString("Rol","");
                iniciarSession(selectedUser);
            }
        });
        return vista;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se ha encontrado al usuario", Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        Usuario user=new Usuario();
        Toast.makeText(getContext(), "Se ha encontrado al usuario "+mTextInputEmail.getText().toString(), Toast.LENGTH_SHORT).show();
        JSONArray jsonArray=response.optJSONArray("datos");
        JSONObject jsonObject=null;
        try {
            jsonObject=jsonArray.getJSONObject(0);
            user.setCorreo(jsonObject.optString("usu_usuario"));
            user.setContra(jsonObject.optString("usu_password"));
            user.setNombre(jsonObject.optString("Nombre"));
            user.setId(jsonObject.optString("idUsuario"));
            user.setRol(jsonObject.optString("Rol"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String Rol =user.getRol();
        //oast.makeText(getContext(), user.getRol(), Toast.LENGTH_SHORT).show();
        if (Rol.equals(selectedUser)) {
            if (selectedUser.equals("Cliente")) {
                Intent intent = new Intent(getContext(), MapClientActivity.class);
                intent.putExtra(MapClientActivity.nombres, user.getId());
                mDialog.dismiss();
                startActivity(intent);

            } else if (selectedUser.equals("Conductor")) {
                Intent intent = new Intent(getContext(), MapDriverActivity.class);
                intent.putExtra(MapDriverActivity.nombres, user.getId());
                SharedPreferences.Editor editor=mPref.edit();
                editor.putString("usuario",user.getNombre());
                editor.apply();
                mDialog.dismiss();
                startActivity(intent);
            }
        }else {
            Toast.makeText(getContext(), "Tiene que elejir la opcion soy " + Rol, Toast.LENGTH_SHORT).show();
        }
    }
    public void iniciarSession(String selectedUser){
        mDialog.show();
        String url="https://agleam-money.000webhostapp.com/test/ejemploBDRemota/sesion.php?usu_usuario="+mTextInputEmail.getText().toString()+"&usu_password="+mTextInputPassword.getText().toString()+"&Rol="+selectedUser;
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }
}