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
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WebSocketActivity extends AppCompatActivity {

    public final String TAG = "WebSocketClient";
    Button connect,disconnect,send;
    EditText getIP,getMessage;
    WebSocketClient client;
    ListView messageList;
    ArrayList<String> messages;
    ArrayAdapter adapter;
    List<BasicNameValuePair> extraHeaders = null;

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
                client = new WorkClient(URI.create("ws://"+getIP.getText().toString()));
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
                client.close();
                uiDisconnect();
            }
        });

    }

    private void uiConnect()
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

    private void uiDisconnect()
    {
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

    private void uiSend(final String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(message);
            }
        });
    }

    private class WorkClient extends WebSocketClient {

        public final String TAG = "WebSocketClient";

        public WorkClient(URI serverUri, Draft draft) {
            super(serverUri, draft);
        }

        public WorkClient(URI serverURI) {
            super(serverURI);
        }

        public WorkClient(URI serverUri, Map<String, String> httpHeaders) {
            super(serverUri, httpHeaders);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            uiConnect();
            Log.d(TAG,"Connected");
        }

        @Override
        public void onMessage(String message) {
            System.out.println("received: " + message);
            uiSend(message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            uiDisconnect();
            Log.d(TAG, "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }
    }
}

