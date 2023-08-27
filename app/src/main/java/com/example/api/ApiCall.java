package com.example.api;


import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ApiCall extends Activity {

    //Variable Declarations
    static protected String nickName = null;
    static protected String fullName = null;
    static protected String userName = null;
    static protected String pinNumber = null;
    static protected String mobileNumber = null;
    static protected String upiID = null;
    static protected String balance = null;
    static public boolean registerSucc = false;
    public boolean transfer = false;
    public boolean delete = false;
    static public boolean playing = false;
    static public String languageCode = null;
    static public List<TransactionModel> listdata = new ArrayList<>();

    //Variables For Speech To Text
    public String transscript = "";
    static protected String filename = null;
    MediaRecorder recorder = null;

    //Variables For Text To Speech
    String base64FormattedString = null;


    //ApiCall For Getting User Details
    public void userDetails(String nickname) throws NoSuchPaddingException, NoSuchAlgorithmException {
        JSONObject json = new JSONObject();
        try {
            json.accumulate("action", "details");
            json.accumulate("nick_name", nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Cipher cipher = Cipher.getInstance("Key");

        OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(true).build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(json));
        Request request = new Request.Builder()
                .url("http://events.respark.iitm.ac.in:3001/rp_bank_api")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .header("Connection", "close")
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
            String responsestring = response.body().string();
            response.body().close();
            if (!responsestring.equals("None")) {
                JSONObject res = new JSONObject(responsestring);
                nickName = res.getString("nick_name");
                fullName = res.getString("full_name");
                userName = res.getString("user_name");
                pinNumber = res.getString("pin_number");
                mobileNumber = res.getString("mob_number");
                upiID = res.getString("upi_id");
                registerSucc = true;
            } else {
                registerSucc = false;
            }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    //ApiCall TO Check Balance
    public void balance(String nickname) {
        JSONObject json = new JSONObject();

        try {
            json.accumulate("action", "balance");
            json.accumulate("nick_name", nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(true)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(json));
        Request request = new Request.Builder()
                .url("http://events.respark.iitm.ac.in:3001/rp_bank_api")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .header("Connection", "close")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responsebody = response.body().string();
            response.body().close();

            JSONObject out = new JSONObject(responsebody);
            balance = out.getString("balance");

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }


    //ApiCall To Register New Users
    public void register(String nickname, String fullname, String username, String pinnumber, String mobilenumber, String upiId) {
        JSONObject json = new JSONObject();

        try {
            json.accumulate("action", "register");
            json.accumulate("nick_name", nickname);
            json.accumulate("full_name", fullname);
            json.accumulate("user_name", username);
            json.accumulate("pin_number", pinnumber);
            json.accumulate("mob_number", mobilenumber);
            json.accumulate("upi_id", upiId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(true).build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(json));
        Request request = new Request.Builder()
                .url("http://events.respark.iitm.ac.in:3001/rp_bank_api")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .header("Connection", "close")
                .build();

        registerSucc = false;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            JSONObject res = new JSONObject(response.body().string());
            response.body().close();
            if (res.getString("status").equals("success")) {
                registerSucc = true;
            }
            else
            {
                registerSucc = false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    //ApiCall To Transfer Money
    public void transfer(String amount, String receiver) {
        JSONObject json = new JSONObject();

        try {
            json.accumulate("action", "transfer");
            json.accumulate("amount", Integer.parseInt(amount));
            json.accumulate("from_user", nickName);
            json.accumulate("to_user", receiver);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(true).build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(json));
        Request request = new Request.Builder()
                .url("http://events.respark.iitm.ac.in:3001/rp_bank_api")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .header("Connection", "close")
                .build();

        try {
            Response response = client.newCall(request).execute();

            JSONObject res = new JSONObject(response.body().string());
            if (res.getString("status").equals("success")) {
                transfer = true;
            } else {
                transfer = false;
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }


    //ApiCall To Get Transaction List
    public void transactionlist() {
        JSONObject json = new JSONObject();

        try {
            json.accumulate("action", "history");
            json.accumulate("nick_name", nickName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(true).build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(json));
        Request request = new Request.Builder()
                .url("http://events.respark.iitm.ac.in:3001/rp_bank_api")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .header("Connection", "close")
                .build();

        Response response = null;

        try {
            JSONArray jsonarr = null;
            response = client.newCall(request).execute();
            String responsebody = response.body().string();
            response.body().close();
            jsonarr = new JSONArray(responsebody);
            if (jsonarr != null) {
                for (int i = 0; i < jsonarr.length(); i++) {
                    TransactionModel tm = new TransactionModel();
                    tm.setAmount(jsonarr.getJSONObject(i).getString("amount"));
                    tm.setDate(jsonarr.getJSONObject(i).getString("date"));
                    tm.setTime(jsonarr.getJSONObject(i).getString("time"));
                    tm.setNickname(jsonarr.getJSONObject(i).getString("to-from"));
                    listdata.add(tm);
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }


    //ApiCall To Remove User
    public void remove() {
        JSONObject json = new JSONObject();

        try {
            json.accumulate("action", "remove");
            json.accumulate("nick_name", nickName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(true).build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(json));
        Request request = new Request.Builder()
                .url("http://events.respark.iitm.ac.in:3001/rp_bank_api")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .header("Connection", "close")
                .build();

        Response response = null;
        try
        {
            response = client.newCall(request).execute();
            String responsebody = response.body().string();
            response.body().close();
            JSONObject res = new JSONObject(responsebody);
            if (res.getString("status").equals("success")) {
                delete = true;
            } else {
                delete = false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    //Function To Create folder
    public void createFolder() {
        String folderName = "VVC";
        //Creating Folder
        File file = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
        if (!file.exists())
            file.mkdir();
        else {
            filename = file + "/vvc.mp3";
        }
    }


    //Function To Start Recording
    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(filename);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }


    //Function To Stop Recording
    public void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (RuntimeException e) {
            } finally {
                recorder.reset();
                recorder.release();
                recorder = null;
                SystemClock.sleep(500);
                SpeechToText();
            }
        }
    }


    //ApiCall To Convert Speech To Text
    public void SpeechToText() {

        OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(true).build();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", "vvc.mp3",
                        RequestBody.create(MediaType.parse("application/octet-stream"), new File(filename)))
                .addFormDataPart("vtt", "true")
                .addFormDataPart("language", "english")
                .build();

        Request request = new Request.Builder()
                .url("https://asr.iitm.ac.in/asr/v2/decode")
                .method("POST", body)
                .header("Connection", "close")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responsebody = response.body().string();
            response.body().close();
            JSONObject json = new JSONObject(responsebody);
            if (json.getString("status").equals("success")) {
                transscript = json.getString("transcript");
                System.out.println(transscript);
            }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    //ApiCall To Convert Text to Speech
    public void TextToSpeech(String text,String languageCode) {

        JSONObject jsontts = new JSONObject();
        try{
            jsontts.accumulate("input",text);
            jsontts.accumulate("gender","male");
            jsontts.accumulate("lang",languageCode);
            jsontts.accumulate("alpha","1");
            jsontts.accumulate("segmentwise","true");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(true).build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), String.valueOf(jsontts));

        Request request = new Request.Builder()
                .url("https://asr.iitm.ac.in/ttsv2/tts")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .header("Connection", "close")
                .build();

            Response response = null;

        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            response.body().close();
            JSONObject json = new JSONObject(res);
            base64FormattedString = json.getString("audio");
            playMedia();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }


    //Function To Play Media
    public void playMedia() {
        try{
            String url = "data:audio/wav;base64,"+base64FormattedString;
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setVolume(100f, 100f);
                mediaPlayer.setLooping(false);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    playing = true;
                    player.start();
                    Log.d("tts", "onPrepared: "+ player.isPlaying());
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    playing = false;
                    mp.reset();
                    mp.release();
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
