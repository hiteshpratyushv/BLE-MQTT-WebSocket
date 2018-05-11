package com.sendrecv.ble.blesendandrecieve;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.UUID;

public class SendActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    Context context = this;
    BluetoothLeAdvertiser advertiser;
    EditText getData;
    Button startSending, stopSending;
    AdvertiseSettings aSettings;
    ParcelUuid pUuid;
    BluetoothAdapter btAdapter;
    BluetoothManager btManager;
    AdvertiseCallback advertisingCallback;
    AdvertiseData aData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        getData = (EditText) findViewById(R.id.getData);
        startSending = (Button) findViewById(R.id.startSending);
        stopSending = (Button) findViewById(R.id.stopSending);

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        aSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
                .setConnectable(false)
                .build();

        pUuid = new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)));

        advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                ((TextView) findViewById(R.id.advertising)).setVisibility(View.VISIBLE);
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Toast.makeText(context, "Advertising onStartFailure: " + errorCode, Toast.LENGTH_SHORT).show();
                Log.e("BLE", "Advertising onStartFailure: " + errorCode);
                super.onStartFailure(errorCode);
            }
        };

        startSending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = getData.getText().toString();
                if (data.equals("")) {
                    Toast.makeText(context, "Enter Some Text", Toast.LENGTH_SHORT).show();
                    return;
                }
                int i = 1;
                aData = new AdvertiseData.Builder()
                        .setIncludeDeviceName(false)
                        .addServiceData(pUuid, data.getBytes(Charset.forName("UTF-8")))
                        .build();

                advertiser.startAdvertising(aSettings, aData, advertisingCallback);

            }
        });

        stopSending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advertiser.stopAdvertising(advertisingCallback);
                ((TextView) findViewById(R.id.advertising)).setVisibility(View.INVISIBLE);
            }
        });

    }
}
