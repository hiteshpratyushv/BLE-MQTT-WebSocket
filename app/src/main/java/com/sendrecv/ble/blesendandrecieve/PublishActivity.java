package com.sendrecv.ble.blesendandrecieve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.InputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class PublishActivity extends AppCompatActivity {

    Button pubConnect, pubDisconnect, pubPub;
    EditText messagemqtt, ipinputpublish;
    String clientId;
    MqttAndroidClient client;
    MqttConnectOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        pubConnect = (Button) findViewById(R.id.pubConnect);
        pubPub = (Button) findViewById(R.id.pubpub);
        pubDisconnect = (Button) findViewById(R.id.pubDisconnect);
        messagemqtt = (EditText) findViewById(R.id.messagemqtt);
        ipinputpublish = (EditText) findViewById(R.id.ipinputpublish);
        clientId = MqttClient.generateClientId();

        pubConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    client = new MqttAndroidClient(getApplicationContext(), "ssl://192.168.43.112:1883", clientId);
                    InputStream input = getApplicationContext().getAssets().open("caandpem");
                    options = new MqttConnectOptions();
                    options.setSocketFactory(client.getSSLSocketFactory(input,"password"));
                    IMqttToken token = client.connect(options);
                    token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.d("Connection", "Connected to Broker ");
                            //Toast.makeText(getApplicationContext(),"ConnectiontoMQTTBrokerMade", Toast.LENGTH_SHORT).show();
                            pubConnect.setVisibility(View.INVISIBLE);
                            pubDisconnect.setVisibility(View.VISIBLE);
                            pubPub.setVisibility(View.VISIBLE);
                            ipinputpublish.setVisibility(View.INVISIBLE);
                            messagemqtt.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                           // Toast.makeText(getApplicationContext(),"ConnectiontoMQTTBrokerRejected", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
                            Log.e("Connection Error", exception.toString());
                            Log.d("Connection", "Unable to connect to Broker");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        pubDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
                messagemqtt.setVisibility(View.INVISIBLE);
            }
        });

        pubPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payload = messagemqtt.getText().toString();
                byte[] encodedPayload;
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(getString(R.string.topic), message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pubConnect.getVisibility() == View.INVISIBLE)
            disconnect();
    }

    public void disconnect() {
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    pubPub.setVisibility(View.INVISIBLE);
                    pubDisconnect.setVisibility(View.INVISIBLE);
                    pubConnect.setVisibility(View.VISIBLE);
                    ipinputpublish.setVisibility(View.VISIBLE);
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
}
