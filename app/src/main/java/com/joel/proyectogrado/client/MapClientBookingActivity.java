package com.joel.proyectogrado.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.joel.proyectogrado.Activitys.UpdateinfoActivity;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.providers.GoogleApiProvider;
import com.joel.proyectogrado.utils.DecodePoints;

import org.json.JSONArray;
import org.json.JSONException;
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
    RequestQueue requestQueue;
    private List<LatLng> mPolylineList;
    private Marker mMarker;
    private Handler mHandler=new Handler();
    private LocationRequest mLocationRequest;
    private TextView mTextViewClientBooking;
    private TextView mTextViewEmailClientBooking;
    private TextView mtextviewPhoneClientBooking;
    private TextView mtextviewAdressBooking;
    //public String usuario;
    //public static final String nombres="names";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client_booking);
       // MyToolbar.show(this, "Cliente", false);
        //Con esto podremos detener la ubicacion
        //mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
       String usuario=getIntent().getStringExtra("usuario");
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mTextViewEmailClientBooking=findViewById(R.id.textviewEmailClientBooking);
        mTextViewClientBooking=findViewById(R.id.textviewClientBooking);
        mtextviewPhoneClientBooking=findViewById(R.id.textviewPhoneClientBooking);
        mtextviewAdressBooking=findViewById(R.id.textviewAdressBooking);
        mGoogleApiProvider = new GoogleApiProvider(MapClientBookingActivity.this);
        buscarUsuario("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_usuario.php?idUsuario=" + usuario + "");
        //findDriver("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_idconductor.php?idUsuario=" + usuario +"");
        Toast.makeText(MapClientBookingActivity.this, usuario+" ", Toast.LENGTH_SHORT).show();
        startRepeating();
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
    public void startRepeating(){
        //mHandler.postDelayed(mToastRunnable,5000);
        mToastRunnable.run();
    }
    /*public void stopRepeating(){
        mHandler.removeCallbacks(mToastRunnable);
    }*/
    private Runnable mToastRunnable= new Runnable() {
        @Override
        public void run() {
            String usuario=getIntent().getStringExtra("usuario");
            findDriver("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_idconductor.php?idUsuario=" + usuario +"");
            //buscarConductores("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_conductor.php");
            mHandler.postDelayed(this, 5000);
        }
    };
    public void UpdateCoordinates(LatLng RoutefLatLng,LatLng RouteiLatLng, String Disponibilidad, String Camino){
      if(mMarker!=null){
          mMarker.remove();
      }
      mMarker = mMap.addMarker(new MarkerOptions().position(RoutefLatLng).title("Conductor disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));


                if (Disponibilidad.equals("0") && Camino.equals("Norte")){
                    mMarker.setTitle("Sin espacio "+Camino);
                    mMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.redcarn));
                    }else if(Disponibilidad.equals("0") && Camino.equals("Sud")){
                    mMarker.setTitle("Con espacio "+Camino);
                    mMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.redcars));
                    }else if(Disponibilidad.equals("1") && Camino.equals("Norte")){
                    mMarker.setTitle("Con espacio "+Camino);
                    mMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.greencarn));
                    }else{
                    mMarker.setTitle("Con espacio "+Camino);
                    mMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.greencars));
                    }
            Toast.makeText(MapClientBookingActivity.this, "Actualizando...", Toast.LENGTH_SHORT).show();
                    drawRoute(RouteiLatLng,RoutefLatLng);
                    }




    private void buscarUsuario(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        mTextViewClientBooking.setText(jsonObject.getString("Nombre"));
                        mTextViewEmailClientBooking.setText(jsonObject.getString("Correo"));
                        mtextviewPhoneClientBooking.setText(jsonObject.getString("Telefono"));
                    } catch (JSONException e) {
                        Toast.makeText(MapClientBookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapClientBookingActivity.this, "FALLO LA CONEXION", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void findDriver(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                double latitud,longitud;
                String camino, Disponibilidad;
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Disponibilidad=(jsonObject.getString("Disponibilidad"));
                        camino=(jsonObject.getString("Camino"));
                        latitud=(jsonObject.getDouble("Latitud"));
                        longitud=(jsonObject.getDouble("Longitud"));

                        LatLng RouteiLatLng=new LatLng(-17.36567666429554,-66.15925870045132);
                        LatLng RoutefLatLng=new LatLng(latitud,longitud);
                        //mMarker = mMap.addMarker(new MarkerOptions().position(RoutefLatLng).title("Conductor disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
                        UpdateCoordinates(RoutefLatLng,RouteiLatLng,Disponibilidad,camino);
                    } catch (JSONException e) {
                        Toast.makeText(MapClientBookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapClientBookingActivity.this, "FALLO LA CONEXION", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}