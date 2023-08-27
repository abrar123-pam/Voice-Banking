package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    Button reg;
    EditText nickname, fullname, username, pinnumber, mobilenumber, upiId;

    ApiCall obj = new ApiCall();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        reg = findViewById(R.id.registerrbtn);
        nickname = findViewById(R.id.nickname);
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        pinnumber = findViewById(R.id.pinnumber);
        mobilenumber = findViewById(R.id.mobilenumber);
        upiId = findViewById(R.id.upiId);


        // tts nickname
        nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(obj.languageCode.equals("tamil")){
                                speakText("தயவுசெய்து புனைப்பெயரை உள்ளிடவும்");
                            }else{
                                speakText("please enter nickname");
                            }                        }
                    }).start();
                }
            }
        });

        // tts fullname
        fullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(obj.languageCode.equals("tamil")){
                                speakText("தயவுசெய்து முழுப்பெயரை உள்ளிடவும்");
                            }else{
                                speakText("please enter fullname");
                            }                        }
                    }).start();
                }
            }
        });

        // tts username
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(obj.languageCode.equals("tamil")){
                                speakText("பயனர் பெயரை உள்ளிடவும்");
                            }else{
                                speakText("please enter username");
                            }                        }
                    }).start();
                }
            }
        });

        // tts pinnumber
        pinnumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(obj.languageCode.equals("tamil")){
                                speakText("பின் எண்ணை உள்ளிடவும்");
                            }else{
                                speakText("please enter pin number");
                            }                        }
                    }).start();
                }
            }
        });

        // tts mobilenumber
        mobilenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(obj.languageCode.equals("tamil")){
                                speakText("மொபைல் எண்ணை உள்ளிடவும்");
                            }else{
                                speakText("please enter mobile number");
                            }                        }
                    }).start();
                }
            }
        });

        // tts upiId
        upiId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(obj.languageCode.equals("tamil")){
                                speakText("தயவுசெய்து upi ஐடியை உள்ளிடவும்");
                            }else{
                                speakText("please enter upi id");
                            }                        }
                    }).start();
                }
            }
        });


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        obj.register(nickname.getText().toString(),fullname.getText().toString(),username.getText().toString(),pinnumber.getText().toString(),mobilenumber.getText().toString(), upiId.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                if(obj.registerSucc)
                {
                    Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent ij = new Intent(Register.this, Login.class);
                    startActivity(ij);
                }
                else
                {
                    Toast.makeText(Register.this, "User already Exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void speakText(String balance) {
        // Use the languageCode variable to set the language parameter
        obj.TextToSpeech(balance, obj.languageCode);
    }

}