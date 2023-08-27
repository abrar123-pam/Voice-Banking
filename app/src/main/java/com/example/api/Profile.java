package com.example.api;

import static java.lang.String.format;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    TextView fullname,nickname,username,mobilenumber,upiId;
    ApiCall obj = new ApiCall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        fullname = findViewById(R.id.fullname);
        nickname = findViewById(R.id.nickname);
        username = findViewById(R.id.username);
        mobilenumber = findViewById(R.id.mobilenumber);
        upiId = findViewById(R.id.upiId);


        fullname.setText(obj.fullName);
        nickname.setText(obj.nickName);
        username.setText(obj.userName);
        mobilenumber.setText(obj.mobileNumber);
        upiId.setText(obj.upiID);

        String[] arr = new String[5];

        arr[0] = fullname.getText().toString();
        arr[1] = nickname.getText().toString();
        arr[2] = username.getText().toString();
        arr[3] = mobilenumber.getText().toString();
        arr[4] = upiId.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i< arr.length;i++) {

                    if(!obj.playing) {
                        obj.TextToSpeech(arr[i],obj.languageCode);
                        SystemClock.sleep(500);
                        i++;
                    }
                }
            }
        }).start();
    }
}