package com.sendrecv.ble.blesendandrecieve;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.charset.Charset;
import java.util.UUID;

public class SendActivity extends AppCompatActivity {

    BluetoothLeAdvertiser advertiser;
    EditText getData;
    Button startSending,stopSending;
    AdvertiseSettings aSettings;
    ParcelUuid pUuid;
    AdvertiseCallback advertisingCallback;
    AdvertiseData aData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        getData=(EditText)findViewById(R.id.getData);
        startSending=(Button)findViewById(R.id.startSending);
        stopSending=(Button)findViewById(R.id.stopSending);

        advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        aSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable( false )
                .build();

        pUuid = new ParcelUuid( UUID.fromString( getString( R.string.ble_uuid ) ) );

        advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
                super.onStartFailure(errorCode);
            }
        };

        startSending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = getData.getText().toString();
                int i=1;
                aData = new AdvertiseData.Builder()
                        .setIncludeDeviceName( true )
                        .addServiceUuid( pUuid )
                        .addServiceData( pUuid, "Data".getBytes( Charset.forName( "UTF-8" ) ) )
                        .build();

                advertiser.startAdvertising( aSettings, aData, advertisingCallback );

            }
        });

        stopSending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advertiser.stopAdvertising(advertisingCallback);
            }
        });

    }
}
