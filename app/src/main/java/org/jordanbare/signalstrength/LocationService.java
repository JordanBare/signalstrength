package org.jordanbare.signalstrength;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;


/**
 * Created by jordanbare on 1/5/18.
 */

public class LocationService extends Service{

    private LocationListener mListener;
    private LocationManager mLocationManager;
    private String mProvider;
    private Criteria mCriteria;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(){
        establishCriteria();
         mListener = new LocationListener() {
             @Override
             public void onLocationChanged(Location newLocation) {
                 Intent intent = new Intent("org.jordanbare.signalstrength.location_update");
                 intent.putExtra("location", newLocation);
                 sendBroadcast(intent);
             }

             @Override
             public void onStatusChanged(String provider, int status, Bundle extras) {

             }

             @Override
             public void onProviderEnabled(String provider) {
                 updateProviderAndListener();
             }

             @Override
             public void onProviderDisabled(String provider) {
                 Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(intent);
             }

         };
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        updateProviderAndListener();
    }

    private void establishCriteria() {
        mCriteria = new Criteria();
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        mCriteria.setCostAllowed(true);
        mCriteria.setAltitudeRequired(false);
        mCriteria.setBearingRequired(false);
    }

    @SuppressLint("MissingPermission")
    private void updateProviderAndListener() {
        mProvider = mLocationManager.getBestProvider(mCriteria, true);
        mLocationManager.requestLocationUpdates(mProvider, 1000, 1, mListener);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mLocationManager != null){
            mLocationManager.removeUpdates(mListener);
            mLocationManager = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
