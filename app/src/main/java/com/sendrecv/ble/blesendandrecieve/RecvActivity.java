package com.sendrecv.ble.blesendandrecieve;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

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

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        scanResult = intent.getExtras().getParcelable("Device");
        pUuid = new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)));
        byte[] data = scanResult.getScanRecord().getServiceData(pUuid);
        try {
            text = new String(data, "UTF-8");
        } catch (Exception e) {
            text = "No Data Found";
        }
        tv = (TextView) findViewById(R.id.tv);
        tv.setText(text);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
