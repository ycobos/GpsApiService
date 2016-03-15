package com.example.yesid.gpsapiservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvlat = (TextView) findViewById(R.id.tvLat);

        //final Intent intent = new Intent(MainActivity.this, MyService.class);
        // Filtro de acciones que serán alertadas
        IntentFilter filter = new IntentFilter("getCoordinates");

        // Crear un nuevo ResponseReceiver
        ResponseReceiver receiver =
                new ResponseReceiver();
        // Registrar el receiver y su filtro
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

    }

    public void stopService(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    public void startService(View view) {

        Intent intent = new Intent(this, MyService.class);
        startService(intent);

    }

    private class ResponseReceiver extends BroadcastReceiver{
        private ResponseReceiver(){
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()){
                case "getCoordinates":
                    double latitud = intent.getDoubleExtra("Latitud",0.0);
                    double longitud = intent.getDoubleExtra("Longitud",0.0);

                    TextView tvlati = (TextView) findViewById(R.id.tvLat);
                    TextView tvlong = (TextView) findViewById(R.id.tvLong);
                    tvlati.setText(latitud + "");
                    tvlong.setText(longitud+"");


                    break;
            }


        }
    }
}
