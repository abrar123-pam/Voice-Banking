package com.example.api;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class test extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        new NetworkRequestTask().execute();
    }

    class NetworkRequestTask extends AsyncTask<Void, Void, String> {
        private final String TAG = "MainActivity";
        private final String API_URL = "https://events.respark.iitm.ac.in:5000/rp_bank_api";

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Create payload as JSON
                String payload = "{\"action\":\"details\",\"api_token\":\"rp-z8mmlcghm0l1z5a\",\"nick_name\":\"sathakians\"}";

                // Encrypt payload
                String encryptedPayload = rsaEncrypt(payload);

                // Set request headers
                HttpsURLConnection connection = (HttpsURLConnection) new URL(API_URL).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/text");
                connection.setDoOutput(true);

                // Send encrypted payload in request body
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(encryptedPayload.getBytes());
                outputStream.close();

                // Read response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Decrypt response
                String decryptedResponse = rsaDecrypt(response.toString());

                // Return the decrypted response
                return decryptedResponse;
            } catch (Exception e) {
                Log.e("Apiv2", "Error executing network request: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String decryptedResponse) {
            // Handle the decrypted response here
            if (decryptedResponse != null) {
                System.out.println(decryptedResponse);
                Log.d("Apiv2", "Decrypted response: " + decryptedResponse);
            } else {
                Log.d("Apiv2", "Failed to get decrypted response");
            }
        }

        private String rsaEncrypt(String payload) throws Exception {
            String publicKeyContent = readFileAsString("file:///android_asset/public_key.pem");
            publicKeyContent = publicKeyContent.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replace("\n", "");
            byte[] keyBytes = Base64.decodeBase64(publicKeyContent);
            AsymmetricKeyParameter publicKey = PublicKeyFactory.createKey(keyBytes);

            RSAEngine rsaEngine = new RSAEngine();
            PKCS1Encoding rsaEncoding = new PKCS1Encoding(rsaEngine);
            rsaEncoding.init(true, publicKey);

            byte[] encryptedBytes = rsaEncoding.processBlock(payload.getBytes(), 0, payload.getBytes().length);
            return Base64.encodeBase64String(encryptedBytes);
        }

        private String rsaDecrypt(String encryptedData) throws Exception {
            String privateKeyContent = readFileAsString("file:///android_asset/private_key.pem");
            privateKeyContent = privateKeyContent.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("\n", "");
            byte[] keyBytes = Base64.decodeBase64(privateKeyContent);
            AsymmetricKeyParameter privateKey = PrivateKeyFactory.createKey(keyBytes);

            RSAEngine rsaEngine = new RSAEngine();
            PKCS1Encoding rsaEncoding = new PKCS1Encoding(rsaEngine);
            rsaEncoding.init(false, privateKey);

            byte[] encryptedBytes = Base64.decodeBase64(encryptedData);
            byte[] decryptedBytes = rsaEncoding.processBlock(encryptedBytes, 0, encryptedBytes.length);
            return new String(decryptedBytes);
        }

        private String readFileAsString(String filePath) throws IOException {
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            return content.toString();
        }
    }
}
