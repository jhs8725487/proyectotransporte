package com.joel.proyectogrado.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.providers.GoogleApiProvider;
import com.joel.proyectogrado.utils.DecodePoints;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import include.MyToolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapClientBookingActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    SupportMapFragment mMapFragment;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private FusedLocationProviderClient mFusedLocation;
    private GoogleApiProvider mGoogleApiProvider;
    private PolylineOptions mPolylineOptions;
    private List<LatLng> mPolylineList;
    private LocationRequest mLocationRequest;
    private TextView mTextViewClientBooking;
    private TextView mTextViewEmailClientBooking;
    //public static final String nombres="names";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client_booking);
       // MyToolbar.show(this, "Cliente", false);
        //Con esto podremos detener la ubicacion
        //mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mTextViewEmailClientBooking=findViewById(R.id.textviewEmailClientBooking);
        mTextViewClientBooking=findViewById(R.id.textviewClientBooking);
        mGoogleApiProvider = new GoogleApiProvider(MapClientBookingActivity.this);
       // mPref= getSharedPreferences("typeUser", Context.MODE_PRIVATE);
        //String usuario=getIntent().getStringExtra("names");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
         mMap.setMyLocationEnabled(true);
        //StartLocation();
    }
    private void drawRoute(LatLng Origen, LatLng Destino){
        mGoogleApiProvider.getDirections(Origen,Destino).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body());
                    JSONArray jsonArray=  jsonObject.getJSONArray("routes");
                    JSONObject route= jsonArray.getJSONObject(0);
                    JSONObject polylines=route.getJSONObject("overview_polyline");
                    String points=polylines.getString("points");
                    mPolylineList= DecodePoints.decodePoly(points);
                    mPolylineOptions=new PolylineOptions();
                    mPolylineOptions.color(Color.DKGRAY);
                    mPolylineOptions.width(8f);
                    mPolylineOptions.startCap(new SquareCap());
                    mPolylineOptions.jointType(JointType.ROUND);
                    mPolylineOptions.addAll(mPolylineList);
                    mMap.addPolyline(mPolylineOptions);
                }catch (Exception e){
                    Log.d("error", "error encontrado"+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}