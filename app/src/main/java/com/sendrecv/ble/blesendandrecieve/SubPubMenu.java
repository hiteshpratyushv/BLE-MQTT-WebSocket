package com.sendrecv.ble.blesendandrecieve;

import android.content.Intent;
import android.net.wifi.aware.SubscribeConfig;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SubPubMenu extends AppCompatActivity {

    Button sub,pub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_pub_menu);

        sub=(Button)findViewById(R.id.subscribe);
        pub=(Button)findViewById(R.id.publish);

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subI = new Intent(getApplicationContext(), SubscribeActivity.class);
                startActivity(subI);
            }
        });

        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pubI = new Intent(getApplicationContext(),PublishActivity.class);
                startActivity(pubI);
            }
        });
    }
}
