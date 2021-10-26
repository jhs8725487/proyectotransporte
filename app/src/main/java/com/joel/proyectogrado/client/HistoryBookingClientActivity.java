package com.joel.proyectogrado.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.adapters.Historial;
import com.joel.proyectogrado.adapters.HistoryBookingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import include.MyToolbar;

public class HistoryBookingClientActivity extends AppCompatActivity {

    ArrayList<Historial> listaHistorial;
    private RecyclerView mRecyclerView;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_booking_client);
        MyToolbar.show(this,"Historial de viajes",true);
        mRecyclerView=findViewById(R.id.recyclerviewHistoryBooking);
        listaHistorial=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //llenarHistorial();
        buscarConductores("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/mostrar_historial.php");
       // HistoryBookingAdapter adapter= new HistoryBookingAdapter(listaHistorial,HistoryBookingClientActivity.this);
        //mRecyclerView.setAdapter(adapter);
    }
    private void registroHistorial(String Origen, String Destino, String Telefono, String Nombre, String foto ) {
        listaHistorial.add(new Historial(Origen,Destino,Telefono,Nombre,foto));
    }
    private void llenarHistorial() {
        listaHistorial.add(new Historial("plaza de las banderas","universidad mayor de sansimon","77481705","joel","https://agleam-money.000webhostapp.com/test/PROYECTOFINAL/uploads/usuarios/1.jpg"));
        listaHistorial.add(new Historial("plaza de las banderas","universidad mayor de sansimon","77481705","joel","https://agleam-money.000webhostapp.com/test/PROYECTOFINAL/uploads/usuarios/1.jpg"));

    }
    public void buscarConductores(String URL){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                ArrayList <String>Nombre;
                ArrayList <String>Origen;
                ArrayList <String>Destino;
                ArrayList <String>Telefono;
                ArrayList <String> foto;
                Nombre= new ArrayList<>();
                Origen=new ArrayList<>();
                Destino=new ArrayList<>();
                Telefono=new ArrayList<>();
                foto=new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Nombre.add(jsonObject.getString("Nombre"));
                        Origen.add(jsonObject.getString("Origen"));
                        Destino.add(jsonObject.getString("Destino"));
                        Telefono.add(jsonObject.getString("Telefono"));
                        foto.add(jsonObject.getString("foto"));
                        registroHistorial(Origen.get(i),Destino.get(i),Telefono.get(i),Nombre.get(i),foto.get(i));
                        Toast.makeText(HistoryBookingClientActivity.this, Origen.get(i)+" "+Nombre.get(i), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {

                        Toast.makeText(HistoryBookingClientActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                HistoryBookingAdapter adapter= new HistoryBookingAdapter(listaHistorial,HistoryBookingClientActivity.this);
                mRecyclerView.setAdapter(adapter);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HistoryBookingClientActivity.this, "FALLO LA CONEXION", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


}