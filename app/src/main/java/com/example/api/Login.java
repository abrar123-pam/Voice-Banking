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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class Login extends AppCompatActivity {

    Button btn,btn2,micBtn;
    EditText userinput,pininput;
    ProgressBar progressbar;
    ApiCall obj = new ApiCall();
    private MediaPlayer mediaPlayer;
    Animation pulseAnimation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userinput = findViewById(R.id.userinput);
        pininput = findViewById(R.id.pininput);
        btn = findViewById(R.id.registerrbtn);
        btn2 = findViewById(R.id.loginbtn);
        micBtn = findViewById(R.id.micBtnlogin);
        progressbar = findViewById(R.id.progressbar);

        pulseAnimation =  AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click_sound);


        //Login Button Logic
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(userinput.getText().toString().isEmpty() || pininput.getText().toString().isEmpty())) {
                    new asyncCall(Login.this).execute();
                }
                else {
                    Toast.makeText(Login.this,"Please Enter Credentials",Toast.LENGTH_SHORT).show();
                }
              }
        });

        //Register Button Logic
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        //Username Text To Speech
        userinput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (obj.languageCode.equals("tamil")) {
                                speakText("புனைப்பெயரை உள்ளிடவும்");
                            } else {
                                speakText("Please enter your username");
                            }
                        }
                    }).start();
                }
            }
        });

        //Password Text To Speech
        pininput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (obj.languageCode.equals("tamil")) {
                                speakText("பின் எண்ணை உள்ளிடவும்");
                            } else {
                                speakText("Please enter your PIN number");
                            }
                        }
                    }).start();
                }
            }
        });


        //Mic Button Listener
        micBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.startAnimation(pulseAnimation);
                    Toast.makeText(Login.this, "Speak Command", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(Login.this, "Command Ended", Toast.LENGTH_SHORT).show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            obj.stopRecording();

                            if (obj.transscript.contains("register") || obj.transscript.contains("create") || obj.transscript.contains("new"))
                            {
                                Intent intent = new Intent(Login.this, Register.class);
                                startActivity(intent);

                            }
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });
    }

    private void speakText(String text) {
        // Use the languageCode variable to set the language parameter
        obj.TextToSpeech(text, obj.languageCode);
    }


    //Async Call For Login
    private class asyncCall extends AsyncTask<Integer,Integer,String>
    {
        private WeakReference<Login> loginWeakReference;

        asyncCall(Login activity)
        {
            loginWeakReference = new WeakReference<Login>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Login activity = loginWeakReference.get();
            if(activity == null || activity.isFinishing())
            {
                return;
            }
            activity.progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                obj.userDetails(userinput.getText().toString());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Login activity = loginWeakReference.get();
            if(activity == null || activity.isFinishing())
            {
                return;
            }

            activity.progressbar.setVisibility(View.INVISIBLE);

            if(obj.registerSucc)
            {
                if(obj.pinNumber != null && obj.pinNumber.equals(pininput.getText().toString())) {

                    Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, HomePage.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(activity, "Invalid Pin", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
