package com.example.api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TransferMoney extends AppCompatActivity {

    EditText amt,pinnumber,rnickname;
    Button transfer;

    ApiCall obj = new ApiCall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_money);

        amt = findViewById(R.id.amount);
        pinnumber = findViewById(R.id.pinnumber);
        rnickname = findViewById(R.id.reciver);
        transfer = findViewById(R.id.transfer);


        amt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(obj.languageCode.equals("tamil")){
                                speakText("தொகையை உள்ளிடவும்");
                            }else{
                                speakText("please enter amount");
                            }                        }
                    }).start();

                }
            }
        });

        rnickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(obj.languageCode.equals("tamil")){
                                speakText("தபெறுநரின் பெயரை உள்ளிடவும்");
                            }else{
                                speakText("please enter receiver's name");
                            }                        }
                    }).start();
                }
            }
        });

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

        transfer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(pinnumber.getText().toString().equals(obj.pinNumber))
                {
                    try{
                        new asyncTransferMoney().execute();
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(TransferMoney.this,"Invalid pin",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void speakText(String text) {
        // Use the languageCode variable to set the language parameter
        obj.TextToSpeech(text, obj.languageCode);
    }


    private class asyncTransferMoney extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            obj.transfer(amt.getText().toString(),rnickname.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(obj.transfer)
                Toast.makeText(TransferMoney.this,"Payment Successful", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(TransferMoney.this,"Invalid Credentials", Toast.LENGTH_SHORT).show();
        }

        //        @Override
//        protected Void doInBackground(Void... voids) {
//
//            obj.transfer(amt.getText().toString(),rnickname.getText().toString());
//
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    if(obj.transfer)
//                        Toast.makeText(TransferMoney.this,"Payment Successful", Toast.LENGTH_LONG).show();
//                    else
//                        Toast.makeText(TransferMoney.this,"Invalid Credentials", Toast.LENGTH_SHORT).show();
//                }
//            });
//            return null;
//        }
    }

}