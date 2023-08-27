package com.example.api;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "PERMISSION_TAG";
    private int audio = 1;
    private int write = 1;
    private int read = 1;

    ImageView imageView;
    TextView textView1,textView2;
    Animation top,bottom;
    ApiCall obj = new ApiCall();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        imageView = findViewById(R.id.splashmic);
        textView1 = findViewById(R.id.vaultertext);
        textView2 = findViewById(R.id.bankingtext);

        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);

        imageView.startAnimation(top);
        textView1.startAnimation(bottom);
        textView2.startAnimation(bottom);


        if (checkPermission()) {
            obj.createFolder();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, SelectLanguage.class);
                    startActivity(intent);
                    finish();
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                },3000);
        } else {
            requestPermission();

        }
    }

    //To Check Permissions
    public boolean checkPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            int audio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

            if( Environment.isExternalStorageManager() && audio == PackageManager.PERMISSION_GRANTED)
                return true;
            else
                return false;

        }
        else
        {
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int audio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED && audio == PackageManager.PERMISSION_GRANTED;
        }
    }


    //To Request Permissions
    private void requestPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R )
        {
            //Android is 11(R) or above
            try{
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityIfNeeded(intent, 101);
            }
            catch(Exception exception) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityIfNeeded(intent,101);
            }

            String[] permission = {Manifest.permission.RECORD_AUDIO};
            storageActivityResultLauncher.launch(permission);

        }
        else
        {
            //Android is below R
            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
            storageActivityResultLauncher.launch(permission);
        }

    }

    //Activity Result Launcher
    private ActivityResultLauncher<String[]> storageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            //here we will check if permissions are granted

            boolean allAreGranted = true;
            for (Boolean isGranted : result.values())
            {
                Log.d(TAG, "onActivityResult: isGranted:" + isGranted);
                allAreGranted = allAreGranted && isGranted;
            }
            if(allAreGranted)
            {
                Toast.makeText(SplashScreen.this,"All permission granted", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, SelectLanguage.class);
                        startActivity(intent);
                        finish();
                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                },1500);
            }
            else
            {
                Toast.makeText(SplashScreen.this,"All or some permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    });
}