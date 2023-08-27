package com.example.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import java.lang.ref.WeakReference;

public class Transaction extends AppCompatActivity {

    RecyclerView temp;
    ApiCall obj = new ApiCall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction);
        temp = findViewById(R.id.recycle);

        try{
            new asyncCall(Transaction.this).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class asyncCall extends AsyncTask<Integer, Integer, String>
    {

        private WeakReference<Transaction> ehomeWeakReference;

        asyncCall(Transaction activity)
        {
            ehomeWeakReference = new WeakReference<Transaction>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Transaction activity = ehomeWeakReference.get();
            if(activity == null || activity.isFinishing())
            {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {
            obj.transactionlist();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Transaction activity = ehomeWeakReference.get();
            if(activity == null || activity.isFinishing())
            {
                return;
            }
            TransactionAdapter adapter = new TransactionAdapter(obj.listdata, Transaction.this);
            temp.setLayoutManager(new GridLayoutManager(Transaction.this,1, LinearLayoutManager.VERTICAL,false));
            temp.setAdapter(adapter);
        }
    }
}