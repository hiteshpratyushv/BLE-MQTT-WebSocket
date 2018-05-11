package com.sendrecv.ble.blesendandrecieve;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RecvActivity extends AppCompatActivity {

    BluetoothAdapter.LeScanCallback leScanCallback;
    ScanResult scanResult;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    ParcelUuid pUuid;
    String text;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recv);
        Intent intent = getIntent();
        scanResult = intent.getExtras().getParcelable("Device");
        pUuid = new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)));
        byte[] data = scanResult.getScanRecord().getServiceData(pUuid);
        try {
            text = new String(data, "UTF-8");
        }
        catch (Exception e)
        {
            text="No Data Found";
        }
        tv = (TextView) findViewById(R.id.tv);
        tv.setText(text);

    }
}
