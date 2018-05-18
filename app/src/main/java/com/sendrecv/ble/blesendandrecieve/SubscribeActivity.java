package com.sendrecv.ble.blesendandrecieve;

import android.net.SSLCertificateSocketFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class SubscribeActivity extends AppCompatActivity {
    Button subConnect,subDisconnect,subSub,subUnsub;
    EditText ipinputsubscribe;
    ListView messageListView;
    ArrayList<String> messageList;
    ArrayAdapter adapter;
    String clientId;
    MqttAndroidClient client;
    MqttConnectOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        subConnect=(Button)findViewById(R.id.subConnect);
        subSub=(Button)findViewById(R.id.subsub);
        subUnsub=(Button)findViewById(R.id.subunsub);
        subDisconnect=(Button)findViewById(R.id.subDisconnect);
        ipinputsubscribe=(EditText)findViewById(R.id.ipinputsubscribe);
        messageListView=(ListView)findViewById(R.id.messageListView);
        messageList=new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,R.layout.message_list,messageList);
        clientId = MqttClient.generateClientId();
        messageListView.setAdapter(adapter);

        subConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = new MqttAndroidClient(getApplicationContext(), "ssl://"+ipinputsubscribe.getText().toString()+
                        ":1883", clientId);
                try {
                    KeyStore ksTrust = KeyStore.getInstance("BKS");
                    InputStream input = getApplicationContext().getAssets().open("keystore.bks");
                    options = new MqttConnectOptions();
                    options.setSocketFactory(client.getSSLSocketFactory(input, "password"));
                    IMqttToken token = client.connect(options);
                    token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.d("Connection", "Connected to Broker ");
                            //Toast.makeText(getApplicationContext(),"ConnectiontoMQTTBrokerMade", Toast.LENGTH_SHORT).show();
                            subConnect.setVisibility(View.INVISIBLE);
                            subDisconnect.setVisibility(View.VISIBLE);
                            subSub.setVisibility(View.VISIBLE);
                            ipinputsubscribe.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            //Toast.makeText(getApplicationContext(),"ConnectiontoMQTTBrokerRejected", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),exception.toString(),Toast.LENGTH_LONG).show();
                            Log.d("Connection Error",exception.toString());
                            Log.d("Connection", "Unable to connect to Broker");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable throwable) {
                        //do nothing
                    }

                    @Override
                    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                        adapter.add(new String(mqttMessage.getPayload()));
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                        //do nothing
                    }
                });
            }
        });

        subDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subSub.getVisibility()==View.INVISIBLE) {
                    subUnsub.setVisibility(View.INVISIBLE);
                    subSub.setVisibility(View.VISIBLE);
                    unsubscribe();
                }
                disconnect();
                subConnect.setVisibility(View.VISIBLE);
                ipinputsubscribe.setVisibility(View.VISIBLE);
            }
        });

        subSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qos = 1;
                try {
                    IMqttToken subToken = client.subscribe(getString(R.string.topic), qos);
                    subToken.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            subSub.setVisibility(View.INVISIBLE);
                            subUnsub.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken,
                                              Throwable exception) {
                            // The subscription could not be performed, maybe the user was not
                            // authorized to subscribe on the specified topic e.g. using wildcards

                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        subUnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unsubscribe();
                subUnsub.setVisibility(View.INVISIBLE);
                subSub.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subSub.getVisibility()==View.INVISIBLE && subConnect.getVisibility()==View.INVISIBLE)
            unsubscribe();
        if(subConnect.getVisibility()==View.INVISIBLE)
            disconnect();
    }

    public void disconnect()
    {
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    subSub.setVisibility(View.INVISIBLE);
                    subDisconnect.setVisibility(View.INVISIBLE);
                    subConnect.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe()
    {
        try {
            IMqttToken unsubToken = client.unsubscribe(getString(R.string.topic));
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
