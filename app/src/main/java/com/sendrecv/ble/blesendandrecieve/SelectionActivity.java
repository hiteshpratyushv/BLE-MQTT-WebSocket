package com.sendrecv.ble.blesendandrecieve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectionActivity extends AppCompatActivity {
    Button send,recv;
    Context con=this;
    String deviceName;
    String deviceMac;
    Intent prevActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        send=(Button)findViewById(R.id.send);
        recv=(Button)findViewById(R.id.recv);
        prevActivity=getIntent();
        deviceMac=prevActivity.getExtras().getString("Mac");
        deviceName=prevActivity.getExtras().getString("Name");
        getActionBar().setTitle(deviceName);
        //getActionBar().setSubtitle(deviceMac);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(con,SendActivity.class);
                startActivity(intent);
            }
        });
        recv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(con,RecvActivity.class);
                startActivity(intent);
            }
        });
    }
}
