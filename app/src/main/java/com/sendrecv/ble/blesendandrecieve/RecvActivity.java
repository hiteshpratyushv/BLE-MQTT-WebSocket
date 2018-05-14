package com.sendrecv.ble.blesendandrecieve;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

public class RecvActivity extends AppCompatActivity {

    ScanResult scanResult;
    BluetoothDevice device;
    ParcelUuid pUuid;
    String text;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recv);

        tv = (TextView) findViewById(R.id.tv);

        Intent intent = getIntent();
        scanResult = (ScanResult) intent.getExtras().get("Device");
        pUuid=new ParcelUuid(UUID.fromString(getString(R.string.ble_uuid)));
        byte[] data=scanResult.getScanRecord().getBytes();
        String msg="";
        for (byte b : data)
            msg += String.format("%02x ", b);
        msg+="\n";
        byte blen;
        byte btype;
        int index=0;
        while(data[index]!=0)
        {
            blen=data[index];
            btype=data[index+1];
            int len=(int)blen;
            int type=(int)btype;
            int textsize=len-1-2-1;
            int uuid;
            text="Len:0x"+Integer.toHexString(len)+"\tType:0x"+Integer.toHexString(type)+"\tUUID:0x"
                    +byteToHex(data[index+3])+byteToHex(data[index+2]) +"\t\t";
            int wStart=index+1+2+1;
            byte[] btext=new byte[textsize];
            for(int i=0;i<textsize;i++)
            {
                btext[i]=data[wStart+i];
            }
            try {
                text=text.concat(new String(btext, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            msg=msg.concat(text+"\n");
            index+=(len+1);
        }
        tv.setText(msg);
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

    public static String byteToHex(byte b) {
        int i = b;
        if(i>=128)
            i = b & 0xFF;
        return Integer.toHexString(i);
    }

}
