package com.example.lab8.Gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class GPSTracker implements LocationListener {
   Context context;

   public GPSTracker(Context context){
       this.context=context;
   }

    @Override
    public void onLocationChanged(Location location) {

    }
    public Location getLocation(){
       if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
           boolean isEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
           if (isEnable) {
               lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
               return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           } else {
               Toast.makeText(context, "Please Enable GPS", Toast.LENGTH_LONG).show();
               return null;
           }
       }
       else{
            Toast.makeText(context,"ADD PERMISSION",Toast.LENGTH_LONG).show();
            return null;
       }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
