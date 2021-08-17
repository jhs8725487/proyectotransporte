package com.joel.proyectogrado.client;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {
    private DatabaseReference mDataBase;
    private GeoFire mGeoFire;
    public GeofireProvider(){
    mDataBase= FirebaseDatabase.getInstance().getReference().child("active_drivers");
    mGeoFire=new GeoFire(mDataBase);
    }
    public void SaveLocation(String idDriver, LatLng latLng){
        mGeoFire.setLocation(idDriver, new GeoLocation(latLng.latitude,latLng.longitude));
    }
    public void removeLocation(String idDriver){
        mGeoFire.removeLocation(idDriver);

    }
public GeoQuery getActiveDrivers(LatLng latLng){
        GeoQuery geoQuery=mGeoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude),2);
        geoQuery.removeAllListeners();
        return geoQuery;
}
}
