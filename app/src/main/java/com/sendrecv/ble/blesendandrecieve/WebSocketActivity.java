package com.sendrecv.ble.blesendandrecieve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.codebutler.android_websockets.WebSocketClient;

import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class WebSocketActivity extends AppCompatActivity {

    public final String TAG = "WebSocketClient";
    Button connect,disconnect,send;
    EditText getIP,getMessage;
    WebSocketClient client;
    ListView messageList;
    ArrayList<String> messages;
    ArrayAdapter adapter;
    List<BasicNameValuePair> extraHeaders = null;
    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);

        connect=(Button)findViewById(R.id.wsconnect);
        send = (Button)findViewById(R.id.wssend);
        disconnect = (Button)findViewById(R.id.wsdisconnect);
        getIP = (EditText)findViewById(R.id.getIP);
        getMessage = (EditText)findViewById(R.id.getMessage);
        messageList = (ListView)findViewById(R.id.messageList);
        messages=new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,R.layout.message_list,messages);
        messageList.setAdapter(adapter);


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = new WebSocketClient(URI.create("ws://"+getIP.getText().toString()), new WebSocketClient.Listener() {

                    @Override
                    public void onConnect() {
                        Log.d(TAG, "Connected!");
                        connect();
                    }

                    @Override
                    public void onMessage(String message) {
                        send(message);
                        Log.d(TAG, String.format("Got string message! %s", message));
                    }

                    @Override
                    public void onMessage(byte[] data) throws UnsupportedEncodingException {
                        send(new String(data,"UTF-8"));
                        Log.d(TAG, String.format("Got binary message! %s", new String(data,"UTF-8")));
                    }

                    @Override
                    public void onDisconnect(int code, String reason) {
                        Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
                        disconnect();
                    }

                    @Override
                    public void onError(Exception error) {
                        Log.e(TAG, "Error -"+error.toString() );
                        disconnect();
                    }
                }, extraHeaders);
                client.connect();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.send(getMessage.getText().toString());
            }
        });


        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.disconnect();
            }
        });

    }

    private void connect()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                send.setVisibility(View.VISIBLE);
                disconnect.setVisibility(View.VISIBLE);
                getMessage.setVisibility(View.VISIBLE);
                connect.setVisibility(View.INVISIBLE);
                getIP.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void disconnect()
    {
        client=null;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                disconnect.setVisibility(View.INVISIBLE);
                send.setVisibility(View.INVISIBLE);
                getMessage.setVisibility(View.INVISIBLE);
                connect.setVisibility(View.VISIBLE);
                getIP.setVisibility(View.VISIBLE);
            }
        });
    }

    private void send(final String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(message);
            }
        });
    }

}
