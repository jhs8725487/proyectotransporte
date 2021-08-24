package com.joel.proyectogrado.drive;

import androidx.annotation.NonNull;
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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joel.proyectogrado.Activitys.MainActivity;
import com.joel.proyectogrado.Activitys.UpdateinfoActivity;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.client.AuthProvider;
import com.joel.proyectogrado.client.GeofireProvider;
import com.joel.proyectogrado.client.MapClientActivity;
import com.joel.proyectogrado.client.RegisterActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import include.MyToolbar;

public class MapDriverActivity extends AppCompatActivity {
    private AuthProvider mAuthProvider;
    private LatLng mCurrentLatLng;
    private GeofireProvider mGeoFireProvider;
    private FusedLocationProviderClient mFusedLocation;
    private LocationRequest mLocationRequest;
    private Marker mMarker;
    SharedPreferences mPref;
    private Button mButtonConnect;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private boolean mIsconnect=false;
    public static final String nombres="names";
    TextView cajaBienvenido;
    TextView tLatitud;
    TextView tLongitud;
    TextView tDireccion;
    String mlatitud, mlongitud;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    //Localizacion del conductor en tiempo real
                    mCurrentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
                    mlatitud=String.valueOf(location.getLatitude());
                    mlongitud=String.valueOf(location.getLongitude());
                    tLatitud.setText("Latitud "+mlatitud);
                    tLongitud.setText("Longitud "+mlongitud);
                    setLocation(location);
                    prueba();
                    if (mMarker != null){
                        mMarker.remove();

                    }
                    updateLocation();
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver);
        MyToolbar.show(this,"Conductor",true);
        mAuthProvider=new AuthProvider();
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mButtonConnect=findViewById(R.id.btnEnruta);
        mGeoFireProvider=new GeofireProvider();
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
        cajaBienvenido=(TextView)findViewById(R.id.txtBienvenido);
        tLatitud=(TextView) findViewById(R.id.txtLatitud);
        tLongitud=(TextView) findViewById(R.id.txtLongitud);
        tDireccion=(TextView) findViewById(R.id.txtdireccion);
        //mPref=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        mPref= getSharedPreferences("typeUser",Context.MODE_PRIVATE);
        String usuario=getIntent().getStringExtra("names");
        cajaBienvenido.setText("BIENVENIDO "+usuario);
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsconnect){
                    disconect();
                    prueba();
                }else{
                    StartLocation();
                    prueba();
                }
            }
        });
    }
    public void prueba(){
        ejecutarServicio("http://192.168.0.15//ejemploBDRemota/insertargps.php");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActive()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                       // mMap.setMyLocationEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    private void updateLocation(){
        if (mAuthProvider.existSession() && mCurrentLatLng!=null){
            mGeoFireProvider.SaveLocation(mAuthProvider.getId(),mCurrentLatLng);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
        } else if (item.getItemId() == R.id.editar_informacion) {
            Update();
        }
        return super.onOptionsItemSelected(item);
    }
    void Update() {
        String usuario=getIntent().getStringExtra("names");
        SharedPreferences.Editor editor =mPref.edit();
        editor.putString("Usuario",usuario);
        editor.commit();
        Intent intent = new Intent(MapDriverActivity.this, UpdateinfoActivity.class);
        startActivity(intent);
    }
    private void StartLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActive()) {
                    mButtonConnect.setText("Desconectarse");
                    mIsconnect=true;
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                } else {
                    ShowAlerDialogNOGPS();
                }
            } else {
                CheckLocationPermission();
            }
        } else {
            if (gpsActive()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                ShowAlerDialogNOGPS();
            }
        }
    }
    private boolean gpsActive() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
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
                                ActivityCompat.requestPermissions(MapDriverActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(MapDriverActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

            }
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
    void logout() {
        disconect();
        mAuthProvider.Logout();
        Intent intent = new Intent(MapDriverActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void setLocation(Location loc){
        if (loc.getLatitude()!=0.0 && loc.getLongitude()!=0.0){
            try {
                Geocoder geocoder=new Geocoder(this, Locale.getDefault());
                List<Address> list=geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(),1);
                if (!list.isEmpty()){
                    Address DirCalle=list.get(0);
                    tDireccion.setText(DirCalle.getAddressLine(0));
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

}
    private void  disconect(){
        if (mFusedLocation!=null){
            mButtonConnect.setText("Conectarse");
            mIsconnect=false;
            mFusedLocation.removeLocationUpdates(mLocationCallback);
            if (mAuthProvider.existSession()) {
                mGeoFireProvider.removeLocation(mAuthProvider.getId());
            }
        }else{
            Toast.makeText(this, "No se puede desconectar", Toast.LENGTH_SHORT).show();
        }
    }

    private void ejecutarServicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            public void onResponse(String response) {
                Toast.makeText(MapDriverActivity.this, "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapDriverActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                String usuario=getIntent().getStringExtra("names");
                if (mIsconnect==false){
                    parametros.put("idUsuario", usuario);
                    parametros.put("Estado", "1");
                }else {
                    parametros.put("Estado", "");
                    parametros.put("idUsuario", usuario);
                    parametros.put("Latitud", tLatitud.getText().toString().trim());
                    parametros.put("Longitud", tLongitud.getText().toString().trim());
                    parametros.put("Direccion", tDireccion.getText().toString().trim());
                }


                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}