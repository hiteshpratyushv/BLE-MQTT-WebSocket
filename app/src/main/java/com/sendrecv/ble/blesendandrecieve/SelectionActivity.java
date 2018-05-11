package com.sendrecv.ble.blesendandrecieve;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity {
    Button send, recv;
    Context con = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        send = (Button) findViewById(R.id.send);
        recv = (Button) findViewById(R.id.recv);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, SendActivity.class);
                startActivity(intent);
            }
        });
        recv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(con, DeviceScanActivity.class);
                startActivity(intent);
            }
        });
    }
}
