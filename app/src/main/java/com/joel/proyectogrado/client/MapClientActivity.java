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
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseError;
import com.google.maps.android.SphericalUtil;
import com.joel.proyectogrado.R;
import com.joel.proyectogrado.Activitys.UpdateinfoActivity;

import com.joel.proyectogrado.Activitys.MainActivity;
import com.joel.proyectogrado.drive.ConductorActivo;
import com.joel.proyectogrado.drive.MapDriverActivity;
import com.joel.proyectogrado.providers.GoogleApiProvider;
import com.joel.proyectogrado.utils.DecodePoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import include.MyToolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapClientActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Marker mMarker;
    private GoogleMap mMap;
    SupportMapFragment mMapFragment;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private Handler mHandler=new Handler();
    private Button mButtonRequest;
    public static final String nombres="names";
    private List<Marker> mDriversMarkers=new ArrayList<>();
    RequestQueue requestQueue;
    private LatLng  mOriginLatLng;
    private LatLng mDestinationLatLng;
    SharedPreferences mPref;
    private GoogleApiProvider mGoogleApiProvider;
    private List<LatLng> mPolylineList;
    public double Latitud=-17.4135865, Longitud=-66.156731219;
    public int User;
    private AutocompleteSupportFragment mAutocomplete;
    private AutocompleteSupportFragment mAutocompleteDestination;
    private String mOrigin;
    private LatLng mOriginLatLng2;
    private String mDestination;
    private LatLng mDestinationLatLng2;
    private LatLng mCurrentLatLng;
    private PlacesClient mPlaces;
    private boolean bandera2=false;
    private boolean mIsconnect=false;
    private boolean mIsFirstTime=true;
    private GoogleMap.OnCameraIdleListener mCameraListener;
    private PolylineOptions mPolylineOptions;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {

                   /* if (mMarker != null){
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
                        limiteSearch();
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
        mGoogleApiProvider = new GoogleApiProvider(MapClientActivity.this);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mPref= getSharedPreferences("typeUser",Context.MODE_PRIVATE);
        String usuario=getIntent().getStringExtra("names");
        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
        }
        mPlaces=Places.createClient(this);
        instanceAutocompleteOrigin();
        instanceAutocompleteDestination();
        onCameraMove();
        mButtonRequest=findViewById(R.id.btnRequestNow);
        //mAuthProvider=new AuthProvider();
       // mGeoFireProvider=new GeofireProvider();
        mButtonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             goToRequestDriver();
            }
        });
    }
    private  void limiteSearch(){
        LatLng northSide= SphericalUtil.computeOffset(mCurrentLatLng,5000,0);
        LatLng sudSide= SphericalUtil.computeOffset(mCurrentLatLng,5000,0);
        mAutocomplete.setCountry("BOL");
        mAutocomplete.setLocationBias(RectangularBounds.newInstance(northSide,sudSide));
        mAutocompleteDestination.setCountry("BOL");
        mAutocompleteDestination.setLocationBias(RectangularBounds.newInstance(northSide,sudSide));
    }
    /*private void goToRequestDriver() {
        if (mOriginLatLng2!=null && mDestinationLatLng !=null) {
            Intent intent = new Intent(MapClientActivity.this, RequestDriverActivity.class);
            intent.putExtra("mOriginLat", mOriginLatLng2.latitude);
            intent.putExtra("mOriginLong", mOriginLatLng2.longitude);
            intent.putExtra("mDestinationLat",mDestinationLatLng.latitude);
            intent.putExtra("mDestinationLong",mDestinationLatLng.longitude);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(MapClientActivity.this, "Elije un lugar de recojida", Toast.LENGTH_SHORT).show();
        }
    }*/
    public void goToRequestDriver(){
        //flag=true;
        final CharSequence[] opciones={"Me dirijo al norte", "Me dirijo al sud"};
        Boolean bandera=true;
        final AlertDialog.Builder alertOpciones= new AlertDialog.Builder(MapClientActivity.this);
        alertOpciones.setTitle("Hacia donde se dirije?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            String usuario=getIntent().getStringExtra("names");
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Me dirijo al norte")){
                    if (mOriginLatLng2!=null && mDestinationLatLng !=null) {
                        Intent intent = new Intent(MapClientActivity.this, RequestDriverActivity.class);
                        intent.putExtra("mOriginLat", mOriginLatLng2.latitude);
                        intent.putExtra("mOriginLong", mOriginLatLng2.longitude);
                        intent.putExtra("mDestinationLat",mDestinationLatLng.latitude);
                        intent.putExtra("mDestinationLong",mDestinationLatLng.longitude);
                        intent.putExtra("Origin",mOrigin);
                        intent.putExtra("Destination",mDestination);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MapClientActivity.this, "Elije un lugar de recojida", Toast.LENGTH_SHORT).show();
                    }
                    //ejecutarServicio("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/insertargps.php?Camino="+"Norte");
                }else if (opciones[i].equals("Me dirijo al sud")){
                    if (mOriginLatLng2!=null && mDestinationLatLng !=null) {
                        Intent intent = new Intent(MapClientActivity.this, RequestDriverActivity.class);
                        intent.putExtra("mOriginLat", mOriginLatLng2.latitude);
                        intent.putExtra("mOriginLong", mOriginLatLng2.longitude);
                        intent.putExtra("mDestinationLat",mDestinationLatLng.latitude);
                        intent.putExtra("mDestinationLong",mDestinationLatLng.longitude);
                        intent.putExtra("Origin",mOrigin);
                        intent.putExtra("Destination",mDestination);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MapClientActivity.this, "Elije un lugar de recojida", Toast.LENGTH_SHORT).show();
                    }
                    //ejecutarServicio("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/insertargps.php?Camino="+"Sud");
                }else{
                    dialogInterface.dismiss();
                }
                //StartLocation();
                //prueba();
            }
        });
        alertOpciones.show();
    }
    private void onCameraMove(){
        mCameraListener=new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    Geocoder geocoder=new Geocoder(MapClientActivity.this);
                    mOriginLatLng2=mMap.getCameraPosition().target;
                    List<Address> addressList=geocoder.getFromLocation(mOriginLatLng2.latitude,mOriginLatLng2.longitude,1);
                    String city=addressList.get(0).getLocality();
                    String country=addressList.get(0).getCountryName();
                    String address=addressList.get(0).getAddressLine(0);
                    mOrigin=address+" "+city+" "+country;
                    mAutocomplete.setText(address+" "+city+" "+country);

                }catch(Exception e) {
                    Log.d("Error:",  "Mensaje de error :" + e.getMessage());
                }
            }
        };
    }

    public void UpdateCoordinates(double latitud, double longitud, int Usuario, String Disponibilidad, String Camino){
        //LatLng driverLatLng = new LatLng(latitud, longitud);
        for (Marker marker: mDriversMarkers){
            if(marker.getTag()!=null){
                if(marker.getTag().equals(Usuario)){
                    /*marker.remove();
                    mDriversMarkers.remove(marker);*/
                    if (bandera2==true) {
                        float distance=getDistance( -17.36567666429554,-66.15925870045132,latitud,longitud);
                        if (distance < 1000) {
                            marker.setPosition(new LatLng(latitud, longitud));
                        }
                        if (distance > 1000) {
                            if (marker.getTag() != null) {
                                if (marker.getTag().equals(Usuario)) {
                                    marker.remove();
                                    mDriversMarkers.remove(marker);
                                    Toast.makeText(MapClientActivity.this, "Desconectando...", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                    }else{
                        marker.setPosition(new LatLng(latitud, longitud));
                    }
                    if (Disponibilidad.equals("0") && Camino.equals("Norte")){
                        marker.setTitle("Sin espacio "+Camino);
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.redcarn));
                    }else if(Disponibilidad.equals("0") && Camino.equals("Sud")){
                        marker.setTitle("Con espacio "+Camino);
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.redcars));
                    }else if(Disponibilidad.equals("1") && Camino.equals("Norte")){
                        marker.setTitle("Con espacio "+Camino);
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.greencarn));
                    }else{
                        marker.setTitle("Con espacio "+Camino);
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.greencars));
                    }
                    //mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Conductor disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
                    //Toast.makeText(MapClientActivity.this, marker.getPosition()+"", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MapClientActivity.this, "Actualizando...", Toast.LENGTH_SHORT).show();
                }
            }
        }
        //Toast.makeText(MapClientActivity.this, latitud+""+longitud+"", Toast.LENGTH_SHORT).show();
       // Toast.makeText(MapClientActivity.this, "Actualizando...", Toast.LENGTH_SHORT).show();
    }
    public void InsertCoordinates(double latitud, double longitud, int Usuario){
        LatLng driverLatLng = new LatLng(latitud, longitud);
        float distance=getDistance( -17.36567666429554,-66.15925870045132,latitud,longitud);
        if (distance<1000 && bandera2==true) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Conductor disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
            marker.setTag(Usuario);
            mDriversMarkers.add(marker);
        }else if(bandera2==false){
            Marker marker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Conductor disponible").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));
            marker.setTag(Usuario);
            mDriversMarkers.add(marker);
        }
    }
    public boolean bandera(int Usuario){
        for(Marker marker: mDriversMarkers){
            if(marker.getTag()!=null){
                if(marker.getTag().equals(Usuario)){
                    //return;
                    return true;
                }
            }
        }
        return false;
    }
    public void onKeyexit(int Usuario){
        for (Marker marker:mDriversMarkers){
            if (marker.getTag()!=null){
                if (marker.getTag().equals(Usuario)){
                    marker.remove();
                    mDriversMarkers.remove(marker);
                    Toast.makeText(MapClientActivity.this, "Desconectando...", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
    public void registroCoordenadas(double latitud,double longitud, int Usuario, String Estado, String Disponibilidad, String Camino){
        this.Latitud=latitud;
        this.Longitud=longitud;
        this.User=Usuario;

        boolean flag= bandera(Usuario);
        if(flag==false && Estado.equals("1")) {
            InsertCoordinates(latitud, longitud, Usuario);
        }else if(Estado.equals("1")){
            UpdateCoordinates(latitud, longitud, Usuario, Disponibilidad, Camino);
        }else{
            onKeyexit(Usuario);
        }
    }
    public void GraficarRuta(double origenLatitud, double origenLongitud, double destinoLatitud, double destinoLongitud){
        this.mOriginLatLng = new LatLng(origenLatitud, origenLongitud);
        this.mDestinationLatLng=new LatLng(destinoLatitud,destinoLongitud);

        double latitudOrigin=-17.4902578, longitudOrigin=-66.1770539;
        double latitudDestin=-17.3701456, longitudDestin=-66.2071849;
        LatLng RouteiLatLng=new LatLng(latitudOrigin,longitudOrigin);
        LatLng RoutefLatLng=new LatLng(latitudDestin,longitudDestin);
        mMap.addMarker(new MarkerOptions().position(RouteiLatLng).title("Villa Isrrael").icon(BitmapDescriptorFactory.fromResource(R.drawable.redflag)));
        mMap.addMarker(new MarkerOptions().position(RoutefLatLng).title("Villa Granado").icon(BitmapDescriptorFactory.fromResource(R.drawable.greenflag)));

       Marker marker= mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Origen").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_red)));
       Marker marker2= mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_blue)));
       marker.setVisible(false);
       marker2.setVisible(false);
        drawRoute(mOriginLatLng,mDestinationLatLng);

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
            buscarConductores("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/buscar_conductor.php");
            mHandler.postDelayed(this, 5000);
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
        } else if (requestCode == SETTINGS_REQUEST_CODE && !gpsActive()) {
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
        //mButtonConnect.setText("Conectarse");
        mIsconnect=false;
        if (mFusedLocation!=null){
            mFusedLocation.removeLocationUpdates(mLocationCallback);
        }
    }
    private void instanceAutocompleteOrigin(){
        mAutocomplete=(AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteOrigin);
        mAutocomplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mAutocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mOrigin=place.getName();
                mOriginLatLng2=place.getLatLng();
            }
        });
    }
    private void instanceAutocompleteDestination(){

        mAutocompleteDestination=(AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteDestination);
        mAutocompleteDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mAutocompleteDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mDestination=place.getName();
                mDestinationLatLng=place.getLatLng();
            }
        });
    }
    private void StartLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActive()) {
                  //  mButtonConnect.setText("Desconectarse");
                    mIsconnect=true;
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
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
            showroute("https://agleam-money.000webhostapp.com/test/ejemploBDRemota/mostrar_ruta.php");
        }else if(item.getItemId()==R.id.conductores_disponibles){
            this.bandera2=false;
            startRepeating();
        }else if(item.getItemId()==R.id.conductores_cercanos){
            this.bandera2=true;
            startRepeating();
        }else if(item.getItemId()==R.id.action_history){
            Intent intent=new Intent(MapClientActivity.this,HistoryBookingClientActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(MapClientActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnCameraIdleListener(mCameraListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);

        StartLocation();
    }
    public void buscarConductores(String URL){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                ArrayList <Double>Lat2;
                ArrayList <Double>Long2;
                ArrayList <Integer>Usuario;
                ArrayList <String>Estado;
                ArrayList <String> Disponibilidad;
                ArrayList <String> Camino;
                Lat2= new ArrayList<>();
                Long2=new ArrayList<>();
                Usuario=new ArrayList<>();
                Estado=new ArrayList<>();
                Disponibilidad=new ArrayList<>();
                Camino = new ArrayList<>();
               for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                            Disponibilidad.add(jsonObject.getString("Disponibilidad"));
                            Camino.add(jsonObject.getString("Camino"));
                            Estado.add(jsonObject.getString("Estado"));
                             Usuario.add(jsonObject.getInt("idUsuario"));
                             Lat2.add(jsonObject.getDouble("Latitud"));
                             Long2.add(jsonObject.getDouble("Longitud"));
                             registroCoordenadas(Lat2.get(i),Long2.get(i),Usuario.get(i),Estado.get(i),Disponibilidad.get(i),Camino.get(i));
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

    public void showroute(String URL){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                ArrayList <Double>origenLat;
                ArrayList <Double>origenLong;
                ArrayList <Double>destinoLat;
                ArrayList <Double>destinoLong;
                origenLat= new ArrayList<>();
                origenLong=new ArrayList<>();
                destinoLat=new ArrayList<>();
                destinoLong=new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                         origenLat.add(jsonObject.getDouble("origenLatitud"));
                         origenLong.add(jsonObject.getDouble("origenLongitud"));
                         destinoLat.add(jsonObject.getDouble("destinoLatitud"));
                         destinoLong.add(jsonObject.getDouble("destinoLongitud"));
                         GraficarRuta(origenLat.get(i),origenLong.get(i),destinoLat.get(i),destinoLong.get(i));
                        //registroCoordenadas(Lat2.get(i),Long2.get(i),Usuario.get(i),Estado.get(i));
                        //Toast.makeText(MapClientActivity.this, origenLat.get(i)+""+origenLong.get(i), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MapClientActivity.this, destinoLat.get(i)+""+destinoLong.get(i), Toast.LENGTH_SHORT).show();

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