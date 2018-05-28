package com.sendrecv.ble.blesendandrecieve;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketActivity extends AppCompatActivity {

    Button start;
    TextView messages;
    OkHttpClient client;

    private final class EchoWebSocketListener extends WebSocketListener{
        private static final int NORMAL_CLOSURE_STATUS =1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hey");
            webSocket.close(NORMAL_CLOSURE_STATUS,"Bye");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving :"+text);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS,null);
            output("Closing : "+code+"/"+reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            output("Error : "+t.getMessage());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);

        start=(Button)findViewById(R.id.wsstart);
        messages=(TextView)findViewById(R.id.wsmessages);

        client=new OkHttpClient();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
    }
    private void start(){
        Request request =new Request.Builder().url("ws://").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket socket = client.newWebSocket(request,listener);

        client.dispatcher().executorService().shutdown();
    }

    private void output(final String text){
        messages.append(text+"\n");
    }
}
