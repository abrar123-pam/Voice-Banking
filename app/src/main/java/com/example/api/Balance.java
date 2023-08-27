package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.ibm.icu.text.RuleBasedNumberFormat;

import java.util.Locale;

public class Balance extends AppCompatActivity {

    TextView username,upiId,balance;
    ApiCall obj = new ApiCall();

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance);

        username = findViewById(R.id.username);
        upiId = findViewById(R.id.upiId);
        balance = findViewById(R.id.balance);

        username.setText("Hi, " + obj.userName);
        upiId.setText(obj.upiID);
        balance.setText(String.format("%.2f",Float.parseFloat(obj.balance)));

        //Read Out Balance
        try {
            new asyncCall().execute();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if(obj.mediaPlayer != null)
//                {
//                    obj.mediaPlayer.stop();
//                    obj.mediaPlayer.reset();
//                    obj.mediaPlayer.release();
//                    obj.mediaPlayer = null;
//                }
//            }
//        }).start();
//    }

    private String toWords(Double str, String language,String Country)
    {
        Locale local = new Locale(language, Country);
        RuleBasedNumberFormat ruleBasedNumberFormat = new RuleBasedNumberFormat(local, RuleBasedNumberFormat.SPELLOUT);
        return ruleBasedNumberFormat.format(str);
    }

    class asyncCall extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            if(obj.languageCode!=null && obj.languageCode.equals("tamil")){
                obj.TextToSpeech("உங்கள் இருப்பு"+(String.format("%.2f",Float.parseFloat(obj.balance))),"tamil");
            }else {
                obj.TextToSpeech("Your balance is"+(String.format("%.2f",Float.parseFloat(obj.balance))),"english");
            }
            return null;
        }
    }
}