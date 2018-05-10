package com.sendrecv.ble.blesendandrecieve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Console;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signin;
    Context con=this;
    SharedPreferences sharedPref;
    SQLiteDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = con.getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        Boolean check=sharedPref.getBoolean("loginDone",false);
        if(check==true)
        {
            Intent intent=new Intent(con,DeviceScanActivity.class);
            startActivity(intent);
            ((Activity)con).finish();
            return ;
        }
        setContentView(R.layout.activity_login);
        appDatabase=openOrCreateDatabase("appDatabase",MODE_PRIVATE,null);
        appDatabase.execSQL("CREATE TABLE IF NOT EXISTS Login(Name VARCHAR,Email VARCHAR,Password VARCHAR);");
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        signin=(Button)findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr=email.getText().toString();
                String passwordStr=password.getText().toString();
                Cursor resultSet = appDatabase.rawQuery("Select * from Login where Email='"+emailStr+"'",null);
                int resultCount=resultSet.getCount();
                if(resultCount==0) {
                    Toast.makeText(con, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                resultSet.moveToFirst();
                int i;
                for(i=0;i<resultCount;i++)
                {
                    String match=resultSet.getString(2);
                    if(passwordStr.equals(match))
                    {
                        Intent intent=new Intent(con,DeviceScanActivity.class);
                        startActivity(intent);
                        ((Activity)con).finish();
                        i=0;
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("loginDone", true);
                        editor.commit();
                        return ;
                    }
                    resultSet.moveToNext();
                }
                Toast.makeText(con, "Wrong Username/Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void register(View view)
    {
        Intent intent=new Intent(con,RegisterActivity.class);
        startActivity(intent);
    }
}

