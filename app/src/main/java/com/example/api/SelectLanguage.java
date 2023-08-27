package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class SelectLanguage extends AppCompatActivity {

    TextView eng,tam;
    ApiCall obj = new ApiCall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_language);

        eng = findViewById(R.id.englishtext);
        tam = findViewById(R.id.tamil);

        //Text To Speech
        new asynCall().execute();

        //English Page Button
        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale =new Locale("en");
                locale.setDefault(locale);
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
                Intent i = new Intent(SelectLanguage.this, Login.class);
                //obj.languageCode="english";
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //Tamil Page Button
        tam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale locale =new Locale("ta");
                locale.setDefault(locale);
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
                Intent ij = new Intent(SelectLanguage.this, Login.class);
                obj.languageCode="tamil";
                ij.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ij);
            }
        });
    }

    //Async Call Of Text To Speech
    private class asynCall extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            obj.TextToSpeech("please select language","english");
            return null;
        }
    }

}