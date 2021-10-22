package com.joel.proyectogrado.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.providers.GoogleApiProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestDriverActivity extends AppCompatActivity {

    private LottieAnimationView mAnimation;
    private TextView mTextviewLookingFor;
    private Button mButtonCancelRequest;
    private GoogleMap mMap;
    SupportMapFragment mMapFragment;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private Marker mMarker;
    private Handler mHandler=new Handler();
    private Button mButtonRequest;
    private boolean mIsconnect=false;
    public static final String nombres="names";
    private List<Marker> mDriversMarkers=new ArrayList<>();
    RequestQueue requestQueue;
    SharedPreferences mPref;
    public double Latitud=-17.4135865, Longitud=-66.156731219;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_driver);
        mAnimation=findViewById(R.id.animation);
        mTextviewLookingFor=findViewById(R.id.textViewLookingFor);
        mButtonCancelRequest=findViewById(R.id.btnCancelRequest);
        mAnimation.playAnimation();
        buscarConductores("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_conductor.php");

    }

    private float getDistance(double deviceLatitude, double deviceLongitude, double rLatitude, double rLongitude){

        Location locationDevice = new Location("Android Device Location.");
        locationDevice.setLatitude(deviceLatitude);
        locationDevice.setLongitude(deviceLongitude);
        //location to compare
        //Location locationValue=new Location();
        Location locationValue = new Location("location value.");
        locationValue.setLatitude(rLatitude);
        locationValue.setLongitude(rLongitude);
        //distance to
        return locationDevice.distanceTo(locationValue);
    }
    public void buscarConductores(String URL){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                int usuario2=0;
                float menor=0;
                ArrayList<Double> Lat2;
                ArrayList <Double>Long2;
                ArrayList <Integer>Usuario;
                ArrayList <String>Estado;
                ArrayList <String> Disponibilidad;
                ArrayList <String> Camino;
                ArrayList<Float> Distancedrivers;
                Lat2= new ArrayList<>();
                Long2=new ArrayList<>();
                Usuario=new ArrayList<>();
                Estado=new ArrayList<>();
                Disponibilidad=new ArrayList<>();
                Camino = new ArrayList<>();
                Distancedrivers = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Disponibilidad.add(jsonObject.getString("Disponibilidad"));
                        Camino.add(jsonObject.getString("Camino"));
                        Estado.add(jsonObject.getString("Estado"));
                        Usuario.add(jsonObject.getInt("idUsuario"));
                        Lat2.add(jsonObject.getDouble("Latitud"));
                        Long2.add(jsonObject.getDouble("Longitud"));
                        if(Estado.get(i).equals("1")) {
                            float distance = getDistance(-17.36567666429554, -66.15925870045132, Lat2.get(i), Long2.get(i));
                            if (i==0){
                                menor=distance;
                                usuario2=Usuario.get(i);
                            }
                            if(distance<menor) {
                                menor = distance;
                                usuario2=Usuario.get(i);
                            }
                        }
                    } catch (JSONException e) {

                        Toast.makeText(RequestDriverActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                double mLatitude =getIntent().getDoubleExtra("mOriginLat",0);
                double mLongitude=getIntent().getDoubleExtra("mOriginLong",0);
                double mLatitude2=getIntent().getDoubleExtra("mDestinationLat",0);
                double mLongitude2=getIntent().getDoubleExtra("mDestinationLong",0);
                //Toast.makeText(RequestDriverActivity.this, mLatitude+" "+mLongitude, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(RequestDriverActivity.this, MapClientBookingActivity.class);
                intent.putExtra("usuario",usuario2+"");
                intent.putExtra("mLatitude",mLatitude);
                intent.putExtra("mLongitude",mLongitude);
                intent.putExtra("mLatitude2",mLatitude2);
                intent.putExtra("mLongitude2",mLongitude2);
                startActivity(intent);
                finish();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RequestDriverActivity.this, "FALLO LA CONEXION", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}