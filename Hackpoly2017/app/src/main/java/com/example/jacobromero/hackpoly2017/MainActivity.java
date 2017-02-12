package com.example.jacobromero.hackpoly2017;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentEmotion;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button submit;
    private EditText inputText;
    private TextView outputText;
    private HashMap<String, Double> emotions = new HashMap<>();
    private HashMap<String, Integer> kV = new HashMap<>();
    private AlchemyLanguage service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kV.put("Anger", 0x1F620);
        kV.put("Fear", 0x1F628);
        kV.put("Disgust", 0x1F631);
        kV.put("Joy", 0x1F603);
        kV.put("Sadness", 0x1F61E);
        kV.put("Neutral", 0x1F610);

        submit = (Button) findViewById(R.id.submitButton);
        inputText = (EditText) findViewById(R.id.myInputText);
        outputText = (TextView) findViewById(R.id.myResponse);
        service = new AlchemyLanguage();
        service.setApiKey("511873582d6e1c27d0ed73ca26ea2f03c759bd48");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundTask bg = new BackgroundTask();
                bg.execute(inputText.getText().toString());
                Log.i("Hello", inputText.getText().toString());
            }
        });

    }

    private void Emoji(String text) {
        int unicode = kV.get(text);
        String emoji = new String(Character.toChars(unicode));
        outputText.setText(emoji);
        outputText.setTextSize(100);
    }

    private class BackgroundTask extends AsyncTask<String, Void, DocumentEmotion> {
        @Override
        protected DocumentEmotion doInBackground(String... params) {
            Map<String, Object> param = new HashMap<>();
            param.put("text", params[0].toString());
            Log.i("Text", "doInBackground: " + params[0].toString());
            DocumentEmotion emotion = service.getEmotion(param).execute();

            return emotion;
        }

        @Override
        protected void onPostExecute(DocumentEmotion result) {
            emotions.put("Anger", result.getEmotion().getAnger());
            emotions.put("Disgust", result.getEmotion().getDisgust());
            emotions.put("Fear", result.getEmotion().getFear());
            emotions.put("Joy", result.getEmotion().getJoy());
            emotions.put("Sadness", result.getEmotion().getSadness());
            emotions.put("Neutral", 0.0);
            Log.i("PostExecute", "Finished" + result.getEmotion().getAnger());

            String myText = "Anger" + result.getEmotion().getAnger() + "\n" +
                    "Disgust" +  result.getEmotion().getDisgust() + "\n" +
                    "Fear" +  result.getEmotion().getFear() + "\n" +
                    "Joy" + result.getEmotion().getJoy() + "\n" +
                    "Sadness" + result.getEmotion().getSadness();

            Log.i("MyText ", myText);

            Object[] keys = emotions.keySet().toArray();
            String largest = "Neutral";
            for(Object k : keys){
                if (emotions.get((String)k) > emotions.get(largest)){
                    largest = (String)k;
                }
            }

            Emoji(largest);

        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
