package com.sendrecv.ble.blesendandrecieve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codebutler.android_websockets.WebSocketClient;

import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;


public class WebSocketActivity extends AppCompatActivity {

    public final String TAG = "WebSocketClient";
    Button connect,disconnect,send;
    TextView messages;
    EditText getIP;
    WebSocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);

        connect=(Button)findViewById(R.id.wsconnect);
        send = (Button)findViewById(R.id.wssend);
        disconnect = (Button)findViewById(R.id.wsdisconnect);
        messages=(TextView)findViewById(R.id.wsmessages);
        getIP = (EditText)findViewById(R.id.getIP);
        List<BasicNameValuePair> extraHeaders = null;

        client = new WebSocketClient(URI.create("ws://"+getIP.getText().toString()), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.d(TAG, "Connected!");
            }

            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));
            }

            @Override
            public void onMessage(byte[] data) throws UnsupportedEncodingException {
                Log.d(TAG, String.format("Got binary message! %s", new String(data,"UTF-8")));
            }

            @Override
            public void onDisconnect(int code, String reason) {
                Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error -"+error.toString() );
            }
        }, extraHeaders);


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.connect();
                send.setVisibility(View.VISIBLE);
                disconnect.setVisibility(View.VISIBLE);
                connect.setVisibility(View.INVISIBLE);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.send("hello!");
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.disconnect();
                disconnect.setVisibility(View.INVISIBLE);
                send.setVisibility(View.INVISIBLE);
                connect.setVisibility(View.VISIBLE);
            }
        });

    }

}
