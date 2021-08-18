package com.joel.proyectogrado;

import android.content.Intent;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.joel.proyectogrado.Activitys.MainActivity;
import com.joel.proyectogrado.drive.MapDriverActivity;
import com.joel.proyectogrado.models.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SessionFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    RequestQueue rq;
    JsonRequest jrq;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //return inflater.inflate(R.layout.fragment_session, container, false);
        View vista=inflater.inflate(R.layout.fragment_session,container,false);
        mTextInputEmail=(TextInputEditText) vista.findViewById(R.id.textInputEmail);
        mTextInputPassword=(TextInputEditText) vista.findViewById(R.id.textInputPassword);
        mButtonLogin=(Button) vista.findViewById(R.id.btnLogin);
        rq= Volley.newRequestQueue(getContext());
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSession();
            }
        });
        return vista;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se ha encontrado al usuario", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Client client=new Client();
        Toast.makeText(getContext(), "Se ha encontrado al usuario "+mTextInputEmail.getText().toString(), Toast.LENGTH_SHORT).show();

        JSONArray jsonArray=response.optJSONArray("datos");
        JSONObject jsonObject=null;
        try {
            jsonObject=jsonArray.getJSONObject(0);
            client.setCorreo(jsonObject.optString("user"));
            client.setContra(jsonObject.optString("pwd"));
            client.setNombre(jsonObject.optString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent=new Intent(getContext(), MapDriverActivity.class);
        intent.putExtra(MapDriverActivity.nombres, client.getNombre());
        startActivity(intent);
    }
    private void iniciarSession(){
        String url="http://192.168.0.11//ejemploBDRemota/sesion.php?Correo="+mTextInputEmail.getText().toString()+"&usu_password="+mTextInputPassword.getText().toString();
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }
}