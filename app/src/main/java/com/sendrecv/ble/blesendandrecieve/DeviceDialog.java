package com.sendrecv.ble.blesendandrecieve;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class DeviceDialog extends Dialog {
    public Context c;
    public Dialog d;
    TextView dispName,dispMac;
    ScanResult result;
    TableLayout table;

    public DeviceDialog(Context c,ScanResult result) {
        super(c);
        this.c = c;
        this.result=result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.device_dialog);
        dispName=(TextView)findViewById(R.id.dispName);
        dispMac=(TextView)findViewById(R.id.dispMac);
        table=(TableLayout)findViewById(R.id.uuidlist);

        dispMac.setText(result.getDevice().getAddress());
        dispName.setText(result.getDevice().getName());

        byte[] data = result.getScanRecord().getBytes();
        byte blen;
        byte btype;
        int index=0;
        int j=0;
        while(data[index]!=0)
        {
            TableRow row= new TableRow(c);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TableRow.LayoutParams tlp1 = new TableRow.LayoutParams(160,TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams tlp2 = new TableRow.LayoutParams(160,TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams tlp3 = new TableRow.LayoutParams(280,TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams tlp4 = new TableRow.LayoutParams(400,TableRow.LayoutParams.WRAP_CONTENT);

            blen=data[index];
            btype=data[index+1];
            int len=(int)blen;
            int type=(int)btype;
            int textsize;
            if(type==22)
                textsize=len-1-2;
            else
                textsize=len-1-2-1;
            TextView lenCell=new TextView(c);
            TextView typeCell=new TextView(c);
            TextView uuidCell=new TextView(c);
            TextView dataCell=new TextView(c);
            lenCell.setLayoutParams(tlp1);
            typeCell.setLayoutParams(tlp2);
            uuidCell.setLayoutParams(tlp3);
            dataCell.setLayoutParams(tlp4);
            lenCell.setText("0x"+Integer.toHexString(len));
            typeCell.setText("0x"+Integer.toHexString(type));
            uuidCell.setText("0x"+byteToHex(data[index+3])+byteToHex(data[index+2]));
            int wStart=index+1+2+1;
            byte[] btext=new byte[textsize];
            for(int i=0;i<textsize;i++)
            {
                btext[i]=data[wStart+i];
            }
            try {
                dataCell.setText(new String(btext, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            row.addView(lenCell);
            row.addView(typeCell);
            row.addView(uuidCell);
            row.addView(dataCell);
            table.addView(row,j++);
            index+=(len+1);
        }
    }
    public static String byteToHex(byte b) {
        int i = b;
        if(i<0) {
            Log.d("Byte",b+"");
            i = b & 0xFF;
            Log.d("Byte",b+"");
        }
        return Integer.toHexString(i);
    }
}
