package com.joel.proyectogrado.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.firebase.database.DatabaseError;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.Activitys.UpdateinfoActivity;

import com.joel.proyectogrado.Activitys.MainActivity;
import com.joel.proyectogrado.drive.ConductorActivo;
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

public class MapClientActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mMapFragment;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private Marker mMarker;
    private AuthProvider mAuthProvider;
    private GeofireProvider mGeoFireProvider;
    private Handler mHandler=new Handler();
    private Button mButtonConnect;
    private boolean mIsconnect=false;
    public static final String nombres="names";
    private List<Marker> mDriversMarkers=new ArrayList<>();
    private LatLng mCurrentLatLng;
    private boolean mIsFirstTime=true;
    RequestQueue requestQueue;
    private double mExtraOriginLat;
    private double mExtraOriginLgn;
    private double mExtraDestinationLat;
    private double mExtraDestinationLng;
    private LatLng  mOriginLatLng;
    private LatLng mDestinationLatLng;
    private LatLng  mOriginLatLng2;
    private LatLng mDestinationLatLng2;
    private LatLng  mOriginLatLng3;
    private LatLng mDestinationLatLng3;
    ConductorActivo conductorActivo;
    SharedPreferences mPref;
    private GoogleApiProvider mGoogleApiProvider;
    private List<LatLng> mPolylineList;
    public double Latitud=-17.4135865, Longitud=-66.156731219;
    private PolylineOptions mPolylineOptions;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {

                    /*if (mMarker != null){
                        mMarker.remove();

                    }*/
                    mCurrentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
                   /* mMarker=mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(), location.getLongitude())
                    )
                            .title("Tu posicion")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconuser))
                    );*/
                    //OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(16f)
                                    .build()
                    ));
                   // updateLocation();
                    if (mIsFirstTime){
                        mIsFirstTime=false;
                        getActiveDrivers();
                    }
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client);
        MyToolbar.show(this, "Cliente", false);
        //Con esto podremos detener la ubicacion
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mPref= getSharedPreferences("typeUser",Context.MODE_PRIVATE);
        String usuario=getIntent().getStringExtra("names");
         /*double mExtraDestinationLat=-17.4902578;
         double mExtraDestinationLng=-66.1770539;
         double mExtraOriginLat=-17.3781992;
         double mExtraOriginLgn=-66.1948988;

        double mExtraDestinationLat2=-17.4902578;
        double mExtraDestinationLng2=-66.1770539;
        double mExtraOriginLat2=-17.4861689;
        double mExtraOriginLgn2=-66.17253721;


        double mExtraOriginLat=-17.4135865;
        double mExtraOriginLgn=-66.156731219;


        double mExtraDestinationLat3=-17.4102568;
        double mExtraDestinationLng3=-66.150937619;
        double mExtraOriginLat3=-17.4135865;
        double mExtraOriginLgn3=-66.156731219;*/
        buscarConductores("http://192.168.0.17//ejemploBDRemota/buscar_conductor.php?idUsuario=3");
        mOriginLatLng = new LatLng(Latitud, Longitud);
        mDestinationLatLng=new LatLng(mExtraDestinationLat,mExtraDestinationLng);

        /*mOriginLatLng2=new LatLng(mExtraOriginLat2,mExtraOriginLgn2);
        mDestinationLatLng2=new LatLng(mExtraDestinationLat2,mExtraDestinationLng2);

        mOriginLatLng3=new LatLng(mExtraOriginLat3,mExtraOriginLgn3);
        mDestinationLatLng3=new LatLng(mExtraDestinationLat3,mExtraDestinationLng3);*/

        mGoogleApiProvider = new GoogleApiProvider(MapClientActivity.this);

        mButtonConnect=findViewById(R.id.btnConnect);
        mAuthProvider=new AuthProvider();
        mGeoFireProvider=new GeofireProvider();
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (mIsconnect){
                    disconect();
                }else{
                    StartLocation();
                }*/
                startRepeating();
                //mMarker.remove();
            }
        });
    }
    public void registroCoordenadas(double latitud,double longitud){
        this.Latitud=latitud;
        this.Longitud=longitud;
        mOriginLatLng = new LatLng(latitud, longitud);
        if (mMarker!=null){
            mMarker.remove();
        }
        mMarker=mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Origen").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

        Toast.makeText(MapClientActivity.this, Latitud+""+Longitud, Toast.LENGTH_SHORT).show();
    }
    public void startRepeating(){
        //mHandler.postDelayed(mToastRunnable,5000);
        mToastRunnable.run();
    }
    public void stopRepeating(){
        mHandler.removeCallbacks(mToastRunnable);
    }
    private Runnable mToastRunnable= new Runnable() {
        @Override
        public void run() {
            //Toast.makeText(MapClientActivity.this, "Patricia Escalera", Toast.LENGTH_SHORT).show();
             buscarConductores("http://192.168.0.17//ejemploBDRemota/buscar_conductor.php?idUsuario=3");
            mHandler.postDelayed(this, 7000);
        }
    };
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActive()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    } else {
                        ShowAlerDialogNOGPS();
                    }
                } else {
                    CheckLocationPermission();
                }
            } else {
                CheckLocationPermission();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActive()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        } else {
            ShowAlerDialogNOGPS();
        }
    }

    private void ShowAlerDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicacion para continuar")
                .setPositiveButton("configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActive() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }
    private void  disconect(){
        mButtonConnect.setText("Conectarse");
        mIsconnect=false;
        if (mFusedLocation!=null){
            mFusedLocation.removeLocationUpdates(mLocationCallback);
        }
    }
    private void StartLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActive()) {
                    mButtonConnect.setText("Desconectarse");
                    mIsconnect=true;
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(false);
                } else {
                    ShowAlerDialogNOGPS();
                }
            } else {
                CheckLocationPermission();
            }
        } else {
            if (gpsActive()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(false);
            } else {
                ShowAlerDialogNOGPS();
            }
        }
    }
    //Este metodo me muestra los conductores activos cerca de un radio determinado
    public void getActiveDrivers(){
        mGeoFireProvider.getActiveDrivers(mCurrentLatLng).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                //Añadimos los marcadores de los conductores que se conecten a la aplicacion
                for (Marker marker: mDriversMarkers){
                    if (marker.getTag()!=null){
                        if (marker.getTag().equals(key)){
                            return;
                        }
                    }
                }
               /* LatLng driverLatLng=new LatLng(location.latitude, location.longitude);
                Marker marker=mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Conductor disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
                marker.setTag(key);
                mDriversMarkers.add(marker);*/

                LatLng driverLatLng=new LatLng(location.latitude, location.longitude);
                Marker marker=mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Conductor disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
                marker.setTag(key);
                mDriversMarkers.add(marker);
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker marker: mDriversMarkers){
                    if (marker.getTag()!=null){
                        if (marker.getTag().equals(key)){
                            marker.remove();
                            mDriversMarkers.remove(marker);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                //Actualizar la posicion de cada conductor
                for (Marker marker: mDriversMarkers){
                    if (marker.getTag()!=null){
                        if (marker.getTag().equals(key)){
                            marker.setPosition(new LatLng(location.latitude,location.longitude));
                        }
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    private void CheckLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MapClientActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(MapClientActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
        } else if (item.getItemId() == R.id.editar_informacion) {
            Update();
        }else if(item.getItemId()== R.id.action_ruta){
            VerRuta();
        }
        return super.onOptionsItemSelected(item);
    }
    void VerRuta(){

    }
    void Update() {
        String usuario=getIntent().getStringExtra("names");
        SharedPreferences.Editor editor =mPref.edit();
        editor.putString("Usuario",usuario);
        editor.commit();
        Intent intent = new Intent(MapClientActivity.this, UpdateinfoActivity.class);
        startActivity(intent);
    }

    void logout() {
        mAuthProvider.Logout();
        Intent intent = new Intent(MapClientActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void updateLocation(){
        mGeoFireProvider.SaveLocation(mAuthProvider.getId(), mCurrentLatLng);

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);


       // startRepeating();
        mMarker=mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Origen").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
       // mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_blue)));

        //mMap.addMarker(new MarkerOptions().position(mOriginLatLng2).title("Origen").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_red)));
        //mMap.addMarker(new MarkerOptions().position(mDestinationLatLng2).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_blue)));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                .target(mOriginLatLng)
                .zoom(14f)
                .build()
        ));
        //drawRoute(mOriginLatLng,mDestinationLatLng);
        //drawRoute(mOriginLatLng2,mDestinationLatLng2);
        //drawRoute(mOriginLatLng3,mDestinationLatLng3);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //mMap.setMyLocationEnabled(true);
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);


    }
    public void buscarConductores(String URL){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                          double Latitud2= jsonObject.getDouble("Latitud");
                          double Longitud2= jsonObject.getDouble("Longitud");
                          registroCoordenadas(Latitud2,Longitud2);
                    } catch (JSONException e) {

                        Toast.makeText(MapClientActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapClientActivity.this, "FALLO LA CONEXION", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}