package com.joel.proyectogrado.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.joel.proyectogrado.Activitys.UpdateinfoActivity;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.providers.GoogleApiProvider;
import com.joel.proyectogrado.utils.DecodePoints;
import com.squareup.picasso.Picasso;

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
    private MediaPlayer mMediaPlayer;
    Polyline polylineFinal;
    private List<LatLng> mPolylineList;
    private LatLng mCurrentLatLng;
    private Marker mMarker, mMarker2;
    private Handler mHandler=new Handler();
    private LocationRequest mLocationRequest;
    private TextView mTextViewClientBooking;
    private TextView mTextViewEmailClientBooking;
    private TextView mtextviewPhoneClientBooking;
    private TextView mtextviewAvailableBooking;
    private TextView mtextviewTime;
    private TextView mtextviewDistance;
    ImageView imageViewBooking;
    private LatLng mOriginLatLong, mDestinationLatLong;
    private Button mButtonStartTravel;
    boolean bandera=false, bandera2=false, bandera3=false;
    private TextView mtextviewAdressBooking;
    private RequestQueue request;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {

                    /*if (mMarker2 != null){
                        mMarker2.remove();

                    }*/
                    //mCurrentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
                    /*mMarker2=mMap.addMarker(new MarkerOptions().position(
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
                    /*if (mIsFirstTime){
                        mIsFirstTime=false;
                    }*/
                }

            }
        }
    };
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
        mtextviewAvailableBooking=findViewById(R.id.textviewAvailableBooking);
        mtextviewTime=findViewById(R.id.textviewTime);
        mtextviewDistance=findViewById(R.id.textviewDistance);
        mButtonStartTravel=findViewById(R.id.btnStartBooking);
        imageViewBooking=findViewById(R.id.imageViewClientBooking);
        request=Volley.newRequestQueue(getApplicationContext());
        double mLatitude =getIntent().getDoubleExtra("mLatitude",0);
        double mLongitude=getIntent().getDoubleExtra("mLongitude",0);
        double mLatitude2=getIntent().getDoubleExtra("mLatitude2",0);
        double mLongitude2=getIntent().getDoubleExtra("mLongitude2",0);
        mOriginLatLong=new LatLng(mLatitude,mLongitude);
        mDestinationLatLong=new LatLng(mLatitude2,mLongitude2);
        mGoogleApiProvider = new GoogleApiProvider(MapClientBookingActivity.this);
        buscarUsuario("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_usuario.php?idUsuario=" + usuario + "");
        //findDriver("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_idconductor.php?idUsuario=" + usuario +"");
        Toast.makeText(MapClientBookingActivity.this, usuario+" ", Toast.LENGTH_SHORT).show();
        startRepeating();
        Picasso.with(MapClientBookingActivity.this).load("https://agleam-money.000webhostapp.com/test/PROYECTOFINAL/uploads/usuarios/1.jpg").into(imageViewBooking);
        mButtonStartTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.addMarker(new MarkerOptions().position(mDestinationLatLong).title("Lugar de destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_blue)));
                bandera3=true;
            }
        });

       // mPref= getSharedPreferences("typeUser", Context.MODE_PRIVATE);
        //String usuario=getIntent().getStringExtra("names");
    }
    @Override
    protected void onStop() {
        super.onStop();

        String usuario=getIntent().getStringExtra("usuario");

        buscarUsuario("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_usuario.php?idUsuario=" + usuario + "");

        //  stopRepeating();
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        if(mMediaPlayer!=null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.addMarker(new MarkerOptions().position(mOriginLatLong).title("Lugar de recojida").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_red)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
         mMap.setMyLocationEnabled(false);
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
        StartLocation();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActive()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(false);
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
    private void CheckLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MapClientBookingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(MapClientBookingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

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
            mMap.setMyLocationEnabled(false);
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
    private void StartLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActive()) {
                    //  mButtonConnect.setText("Desconectarse");
                    //mIsconnect=true;
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
    private void createNotficationChanel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name="reminderNotification";
            String descripcion="chanel for alarm manager";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("ubberClone",name,importance);
            channel.setDescription(descripcion);

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showNotificationActivity(){
        PowerManager pm=(PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if(!isScreenOn){
            PowerManager.WakeLock wakeLock =pm.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.ON_AFTER_RELEASE,
                    "AppName: MyLock"
            );
            wakeLock.acquire(10000);
        }
        Intent intent =new Intent(getBaseContext(),NotificationBookingActivityActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void drawRoute(LatLng Origen, LatLng Destino){
            mGoogleApiProvider.getDirections(Origen, Destino).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("routes");
                        JSONObject route = jsonArray.getJSONObject(0);
                        JSONObject polylines = route.getJSONObject("overview_polyline");
                        String points = polylines.getString("points");
                        mPolylineList = DecodePoints.decodePoly(points);
                        mPolylineOptions = new PolylineOptions();
                        mPolylineOptions.color(Color.DKGRAY);
                        mPolylineOptions.width(8f);
                        mPolylineOptions.startCap(new SquareCap());
                        mPolylineOptions.jointType(JointType.ROUND);
                        mPolylineOptions.addAll(mPolylineList);
                        polylineFinal=mMap.addPolyline(mPolylineOptions);
                        JSONArray legs=route.getJSONArray("legs");
                        JSONObject leg=legs.getJSONObject(0);
                        JSONObject distance=leg.getJSONObject("distance");
                        JSONObject duration=leg.getJSONObject("duration");
                        String distanceText=distance.getString("text");
                        String duracionText=duration.getString("text");
                        mtextviewTime.setText(duracionText);
                        mtextviewDistance.setText(distanceText);
                    } catch (Exception e) {
                        Log.d("error", "error encontrado" + e.getMessage());
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
    public void stopRepeating(){
        mHandler.removeCallbacks(mToastRunnable);
    }
    private Runnable mToastRunnable= new Runnable() {
        @Override
        public void run() {
            String usuario=getIntent().getStringExtra("usuario");
            findDriver("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_idconductor.php?idUsuario=" + usuario +"");
            //buscarConductores("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_conductor.php");
            mHandler.postDelayed(this, 7000);
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


                if(bandera && mOriginLatLong!=null && !bandera3 ){
                    polylineFinal.remove();
                    drawRoute(mOriginLatLong, RoutefLatLng);
                }else if(bandera && mOriginLatLong!=null ){
                    polylineFinal.remove();
                    drawRoute(mDestinationLatLong, RoutefLatLng);
                }
                if(!bandera && mOriginLatLong!=null && !bandera3) {
                    drawRoute(mOriginLatLong, RoutefLatLng);
                    bandera=true;
                }else if(!bandera && mOriginLatLong!=null){
                    drawRoute(mDestinationLatLong, RoutefLatLng);
                    bandera=true;
                }
                if (!bandera3) {
                    float distance = getDistance(mOriginLatLong.latitude, mOriginLatLong.longitude, RouteiLatLng.latitude, RoutefLatLng.longitude);

                    if (distance <= 1000 && !bandera2) {
                        createNotficationChanel();
                        Intent resultIntent = new Intent(this, NotificationBookingActivityActivity.class);
                        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MapClientBookingActivity.this, "ubberClone")

                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("LINEA 111")
                                .setContentText("TU TRANSPORTE SE ENCUENTRA A 1 KILOMETRO")
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setContentIntent(resultPendingIntent);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MapClientBookingActivity.this);
                        notificationManagerCompat.notify(123, builder.build());

                        mMediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
                        mMediaPlayer.setLooping(true);
                        if (mMediaPlayer != null) {
                            if (!mMediaPlayer.isPlaying()) {
                                mMediaPlayer.start();
                            }
                        }
                        //showNotificationActivity();
                        bandera2 = true;
                    }

                    Toast.makeText(MapClientBookingActivity.this, "Actualizando " + distance, Toast.LENGTH_SHORT).show();
                }else{
                   // float distance = getDistance(mDestinationLatLong.latitude, mDestinationLatLong.longitude, RouteiLatLng.latitude, RoutefLatLng.longitude);
                    float distance = getDistance(RoutefLatLng.latitude, RoutefLatLng.longitude, mDestinationLatLong.latitude, mDestinationLatLong.longitude);

                    if (distance <= 200 && !bandera2) {
                        createNotficationChanel();
                        Intent resultIntent = new Intent(this, calification_client.class);
                        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MapClientBookingActivity.this, "ubberClone")

                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("LINEA 111")
                                .setContentText("Llegaste a tu destino")
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setContentIntent(resultPendingIntent);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MapClientBookingActivity.this);
                        notificationManagerCompat.notify(123, builder.build());

                        mMediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
                        mMediaPlayer.setLooping(true);
                        if (mMediaPlayer != null) {
                            if (!mMediaPlayer.isPlaying()) {
                                mMediaPlayer.start();
                            }

                        }
                        //showNotificationActivity();
                        bandera2 = true;
                    }

                    Toast.makeText(MapClientBookingActivity.this, "Actualizando " + distance, Toast.LENGTH_SHORT).show();
                }
        //Toast.makeText(MapClientBookingActivity.this, "Distancia "+distance, Toast.LENGTH_SHORT).show();
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
                        Picasso.with(MapClientBookingActivity.this).load("https://agleam-money.000webhostapp.com/test/PROYECTOFINAL/uploads/usuarios/1.jpg").into(imageViewBooking);
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
                        String Available=(jsonObject.getString("Disponibilidad"));
                        if(Available.equals("0")){
                            mtextviewAvailableBooking.setText("Sin asientos disponibles");
                        }else{
                            mtextviewAvailableBooking.setText("Con asientos disponibles");
                        }

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