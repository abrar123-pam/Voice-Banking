package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    ImageView balance, sendmoney, profile, remove, transactionList;
    TextView username, upiId;
    Button micBtn;
    ProgressBar progressBar;
    ApiCall obj = new ApiCall();
    private MediaPlayer mediaPlayer;
    Animation pulseAnimation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        //Details
        username = findViewById(R.id.headername);
        upiId = findViewById(R.id.headerupiId);

        //Options
        balance = findViewById(R.id.bal);
        sendmoney = findViewById(R.id.sendmny);
        profile = findViewById(R.id.profile);
        remove = findViewById(R.id.removeuser);
        micBtn = findViewById(R.id.michomelogin);
        transactionList = findViewById(R.id.transactionlist);
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click_sound);
        pulseAnimation =  AnimationUtils.loadAnimation(this, R.anim.pulse_animation);


        username.setText("Hi, " + obj.userName);
        upiId.setText(obj.upiID);


        //Mic Button Listener
        micBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.startAnimation(pulseAnimation);
                    Toast.makeText(HomePage.this, "Speak Command", Toast.LENGTH_SHORT).show();

                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            obj.startRecording();
                        }
                    }).start();

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Toast.makeText(HomePage.this, "Command Ended", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            obj.stopRecording();

                            if (obj.transscript.contains("check") || obj.transscript.contains("balance")) {
                                Intent intent = new Intent(HomePage.this, Balance.class);
                                startActivity(intent);
                            } else if (obj.transscript.contains("transfer") || obj.transscript.contains("money")) {
                                Intent intent = new Intent(HomePage.this, TransferMoney.class);
                                startActivity(intent);
                            } else if (obj.transscript.contains("transaction") || obj.transscript.contains("history")) {
                                Intent intent = new Intent(HomePage.this, Transaction.class);
                                startActivity(intent);
                            } else if (obj.transscript.contains("remove") || obj.transscript.contains("delete")) {
                                Intent intent1 = new Intent(HomePage.this, Remove.class);
                                startActivity(intent1);
                            } else if (obj.transscript.contains("profile")) {
                                Intent intent1 = new Intent(HomePage.this, Profile.class);
                                startActivity(intent1);
                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });

        //Send Money Page
        sendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, TransferMoney.class);
                startActivity(intent);
            }
        });

        //Remove Account Page
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rem = new Intent(HomePage.this, Remove.class);
                startActivity(rem);
            }
        });

        //Balance Page
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new asyncBalance().execute();
            }
        });

        //Profile Account page
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(HomePage.this, Profile.class);
                startActivity(in);
            }
        });

        //Transaction List Page
        transactionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, Transaction.class));
            }
        });

    }

    public class asyncBalance extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            try {
                obj.balance(obj.nickName);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent i = new Intent(HomePage.this, Balance.class);
            startActivity(i);
        }
    }

}