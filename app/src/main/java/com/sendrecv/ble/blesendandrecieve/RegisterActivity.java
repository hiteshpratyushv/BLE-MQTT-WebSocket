package com.sendrecv.ble.blesendandrecieve;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText name,email,password;
    Button register;
    Context con=this;
    SQLiteDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText)findViewById(R.id.nameR);
        email=(EditText)findViewById(R.id.emailR);
        password=(EditText)findViewById(R.id.passwordR);
        register=(Button)findViewById(R.id.registerR);
        appDatabase=openOrCreateDatabase("appDatabase",MODE_PRIVATE,null);
        appDatabase.execSQL("CREATE TABLE IF NOT EXISTS Login(Name VARCHAR,Email VARCHAR,Password VARCHAR);");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordStr=password.getText().toString();
                String nameStr=name.getText().toString();
                String emailStr=email.getText().toString();
                if(passwordStr.equals("")||nameStr.equals("")||emailStr.equals("")) {
                    Toast.makeText(con, "Field(s) are empty", Toast.LENGTH_LONG).show();
                }
                else {
                    ContentValues cv=new ContentValues();
                    cv.put("Name",nameStr);
                    cv.put("Email",emailStr);
                    cv.put("Password",passwordStr);
                    appDatabase.insert("login",null,cv);
                    Toast.makeText(con,"Registered",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(con, LoginActivity.class);
                    startActivity(intent);
                    ((Activity) con).finish();
                }
            }
        });
    }

}
