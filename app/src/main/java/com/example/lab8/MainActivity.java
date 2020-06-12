package com.example.lab8;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lab8.Database.Data;
import com.example.lab8.Entity.Contacto;
import com.example.lab8.Gps.GPSTracker;

public class MainActivity extends AppCompatActivity implements SensorEventListener  {

    Double latitud=0.0;
    Double longitud=0.0;
    private ImageView alarm;
    private Toolbar toolbar;
    private MediaPlayer mPlayer;
    boolean sendSms=false;
    private double rootSquare;
    private BroadcastReceiver broadcastReceiver;

    private static final String TAG= "FallDetection";
    private SensorManager sensorManager;
    Sensor accelerometer;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_matricula);
        toolbar.setTitle("SOS");
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.menu_registrar) {
                    Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
                    startActivity(intent);
                    // finish();
                    return true;
                }

                return false;
            }
        });

        alarm = findViewById(R.id.alarm);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                   Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET,Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS,
                    Manifest.permission.CALL_PHONE
            },10);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else{
           // configureButton();
        }

        mPlayer = MediaPlayer.create(this, R.raw.alarm);

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sendSms) {
                    mPlayer.start();
                    sendSms();
                    sendSms=true;
                }
                else{
                    sendSms=false;
                    mPlayer.stop();
                    mPlayer = MediaPlayer.create( MainActivity.this, R.raw.alarm);
                }

            }
        });

       sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
   //   super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode){
          case 10:
          if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
              //configureButton();
              return;
          }

      }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Log.d(TAG, "onSensorChanged: X"+ event.values[0] + "Y: "+ event.values[1] + "Z: "+ event.values[2]);
        //++index;
         float x =  event.values[0];
        float y =  event.values[1];
        float z = event.values[2];

        rootSquare=Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
        if(rootSquare<2.0)
        {

            Toast.makeText(this, "Fall detected", Toast.LENGTH_SHORT).show();
            if(!Data.persona.getContactos().isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Data.persona.getContactos().get(0).getTelefono()));
                startActivity(callIntent);
            }else {
                Toast.makeText(this, "CALLING: LA PIEDAD", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void sendSms(){
      try {

          if(Data.persona.getContactos().isEmpty()){
              Toast.makeText(this,"Por favor agregue contactos ",Toast.LENGTH_LONG).show();
              return;
          }
          SmsManager sms = SmsManager.getDefault();
          GPSTracker gpsTracker= new GPSTracker(this);
          Location l= gpsTracker.getLocation();
          if(l != null){
             String direccion="Latitud: "+l.getLatitude()+ "    Longitud: "+l.getLongitude()+"\n";

             for(Contacto c: Data.persona.getContactos()){
                 String msg="Hola "+c.getNombre()+"\n"+
                         Data.persona.getName()+" esta pidiendo auxilio. Esta es su ubicacion\n"+direccion;


                // sms.sendTextMessage(c.getTelefono(), null,msg, null, null);
             }

          }
          else{
              Log.v("NULL","NULL");
          }

      }
      catch (Exception e){
          Toast.makeText(this,"Error al enviar sms ",Toast.LENGTH_LONG).show();
          Log.e("ALL",e.getMessage());
      }

    }

//    @SuppressLint("MissingPermission")
//    private void configureButton(){
//
//        try {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
//        }
//        catch (Exception e){
//            Log.v("ERROR",e.getMessage());
//        }
//    }
}