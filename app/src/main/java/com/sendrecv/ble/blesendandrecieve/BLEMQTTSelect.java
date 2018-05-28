package com.sendrecv.ble.blesendandrecieve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BLEMQTTSelect extends AppCompatActivity {

    Button mqtt,ble,websocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blemqttselect);

        ble=(Button)findViewById(R.id.ble);
        mqtt=(Button)findViewById(R.id.mqtt);
        websocket = (Button)findViewById(R.id.websocket);

        ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bleI = new Intent(getApplicationContext(),SelectionActivity.class);
                startActivity(bleI);
            }
        });

        mqtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mqttI = new Intent(getApplicationContext(),SubPubMenu.class);
                startActivity(mqttI);
            }
        });

        websocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent websocketI = new Intent(getApplicationContext(),WebSocketActivity.class);
                startActivity(websocketI);
            }
        });
    }
}
