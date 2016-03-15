package com.example.yesid.gpsapiservice;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 30000;
    private static final float LOCATION_DISTANCE = 0;
    public double currentLat, currentLng;
    private SharedPreferences pref;
    private String driverId;


    private GoogleApiClient mGoogleApiClient;
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    private LocationListener locationListener;

    private class LocationListener implements
            com.google.android.gms.location.LocationListener {

        public LocationListener() {
        }

        @Override
        public void onLocationChanged(Location location) {

            currentLat = location.getLatitude();
            currentLng = location.getLongitude();

        }

    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        boolean stopService = false;
        if (intent != null)
            stopService = intent.getBooleanExtra("stopservice", false);

        locationListener = new LocationListener();
        if (stopService)
            stopLocationUpdates();
        else {
            if (!mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();
        }

        //Intent que va a ser enviado por broadcast al main
        Intent localIntent = new Intent("getCoordinates")
                .putExtra("Latitud",currentLat)
                .putExtra("Longitud", currentLng);

        //Emitir el Intent a la actividad
        LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(localIntent);

        return START_STICKY;

    }

    @Override
    public void onCreate() {
        //super.onCreate();

        pref = getSharedPreferences("driver_app", MODE_PRIVATE);
        driverId = pref.getString("driver_id", "");


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, locationListener);

        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(35000);
        mLocationRequest.setFastestInterval(30000);

        //request location
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, locationListener);
    }




    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }

}