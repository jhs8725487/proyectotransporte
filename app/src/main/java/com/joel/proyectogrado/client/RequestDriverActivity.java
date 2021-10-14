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
    private LatLng mCurrentLatLng;
    private boolean mIsFirstTime=true;
    RequestQueue requestQueue;
    private LatLng  mOriginLatLng;
    private LatLng mDestinationLatLng;
    SharedPreferences mPref;
    private GoogleApiProvider mGoogleApiProvider;
    private List<LatLng> mPolylineList;
    public double Latitud=-17.4135865, Longitud=-66.156731219;
    public int User;
    private boolean bandera2=false;
    private PolylineOptions mPolylineOptions;
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
    public void closestDriver(ArrayList<Float> distanceDrivers){
        float menor=distanceDrivers.get(0);
        for(int i=0; i<distanceDrivers.size(); i++){
            if ( distanceDrivers.get(i)<menor){
               menor=distanceDrivers.get(i);
            }
        }
        Toast.makeText(RequestDriverActivity.this, menor+" ", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(RequestDriverActivity.this, MapClientBookingActivity.class);
        startActivity(intent);
        finish();
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
                           //if (distance < 1000) {
                                Distancedrivers.add(distance);
                            //Toast.makeText(RequestDriverActivity.this, "Hola mundo", Toast.LENGTH_SHORT).show();
                            //}
                        }
                        //registroCoordenadas(Lat2.get(i),Long2.get(i),Usuario.get(i),Estado.get(i),Disponibilidad.get(i),Camino.get(i));
                    } catch (JSONException e) {

                        //Toast.makeText(RequestDriverActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                closestDriver(Distancedrivers);
               // Toast.makeText(RequestDriverActivity.this, Distancedrivers.size()+"", Toast.LENGTH_SHORT).show();
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