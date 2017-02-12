package com.example.jacobromero.hackpoly2017;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentEmotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by jacobromero on 2/11/17.
 */
public class ShareActivity extends Activity {
    private HashMap<String, Double> emotions = new HashMap<>();
    private HashMap<String, Integer> kV = new HashMap<>();
    private TextView outputText;
    private AlchemyLanguage service;


    protected void onCreate(Bundle savedInstanceState) {
        service = new AlchemyLanguage();
        service.setApiKey("511873582d6e1c27d0ed73ca26ea2f03c759bd48");

        kV.put("Anger", 0x1F620);
        kV.put("Fear", 0x1F628);
        kV.put("Disgust", 0x1F631);
        kV.put("Joy", 0x1F603);
        kV.put("Sadness", 0x1F61E);
        kV.put("Neutral", 0x1F610);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        outputText = (TextView) findViewById(R.id.myResponseTwo);
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {

            BackgroundTask bg = new BackgroundTask();
            bg.execute(sharedText, outputText);
            // Update UI to reflect text being shared
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
}
