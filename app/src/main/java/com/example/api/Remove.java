package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Remove extends AppCompatActivity {

    Button removeaccount;
    ApiCall obj = new ApiCall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_account);

        removeaccount = findViewById(R.id.removeaccount);


        new Thread(new Runnable() {
            @Override
            public void run() {
                if(obj.languageCode !=null && obj.languageCode.equals("tamil")){
                    speakText(" உங்கள் கணக்கை நீக்க விரும்புகிறீர்களா");
                }else{
                    speakText("would you like to delete your account");
                }            }
        }).start();

        removeaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        obj.remove();
                        try {
                            if(obj.delete) {
                                Intent intent = new Intent(Remove.this, Login.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(Remove.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    private void speakText(String text) {
        // Use the languageCode variable to set the language parameter
        obj.TextToSpeech(text,obj.languageCode);
    }

}