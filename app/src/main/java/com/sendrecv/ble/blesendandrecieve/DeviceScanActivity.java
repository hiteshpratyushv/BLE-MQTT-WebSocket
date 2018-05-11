package com.sendrecv.ble.blesendandrecieve;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceScanActivity extends AppCompatActivity {

    Context context = this;
    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    Button startScanningButton;
    myArrayAdapter myAdapter;
    Button stopScanningButton;
    ListView peripheralListView;
    TextView selectText;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    int count;
    ArrayList<Device> deviceList;
    ArrayList<ScanResult> BLEScanList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);

        selectText = (TextView) findViewById(R.id.selectText);
        selectText.setVisibility(View.INVISIBLE);

        peripheralListView = (ListView) findViewById(R.id.peripheralListView);
        deviceList = new ArrayList<Device>();
        BLEScanList = new ArrayList<ScanResult>();
        count = 0;
        myAdapter = new myArrayAdapter(context, deviceList);
        peripheralListView.setAdapter(myAdapter);

        startScanningButton = (Button) findViewById(R.id.StartScanButton);
        startScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startScanning();
            }
        });

        stopScanningButton = (Button) findViewById(R.id.StopScanButton);
        stopScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopScanning();
            }
        });
        stopScanningButton.setVisibility(View.INVISIBLE);

        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }

        peripheralListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, RecvActivity.class);
                intent.putExtra("Mac", deviceList.get(i).getMac());
                intent.putExtra("Name", deviceList.get(i).getName());
                intent.putExtra("Device", BLEScanList.get(i));
                startActivity(intent);
                stopScanning();
            }
        });
    }

    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            System.out.println(result);
            BluetoothDevice newBLEDevice = result.getDevice();
            String name = newBLEDevice.getName();
            String mac = newBLEDevice.getAddress();
            if (name == null)
                name = "Unknown";
            int i;
            for (i = 0; i < count; i++) {
                Device check = deviceList.get(i);
                if (check.getMac().equals(mac)) {
                    check.setRSSI(result.getRssi());
                    BLEScanList.set(i, result);
                    break;
                }
            }
            if (i == count) {
                Device newDevice = new Device(name, result.getRssi(), mac);
                BLEScanList.add(result);
                deviceList.add(newDevice);
                count++;
            }
            ((myArrayAdapter) peripheralListView.getAdapter()).notifyDataSetChanged();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
            }
        }
    }

    public void startScanning() {
        System.out.println("start scanning");
        startScanningButton.setVisibility(View.INVISIBLE);
        selectText.setVisibility(View.VISIBLE);
        stopScanningButton.setVisibility(View.VISIBLE);
        count = 0;
        deviceList.clear();
        BLEScanList.clear();
        ((myArrayAdapter) peripheralListView.getAdapter()).notifyDataSetChanged();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.startScan(leScanCallback);
            }
        });
    }

    public void stopScanning() {
        System.out.println("stopping scanning");
        startScanningButton.setVisibility(View.VISIBLE);
        stopScanningButton.setVisibility(View.INVISIBLE);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });
    }
}