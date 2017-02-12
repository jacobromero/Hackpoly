package com.hackpoly2017;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentEmotion;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button submit;
    private EditText inputText;
    private TextView outputText;
    private HashMap<String, Double> emotions = new HashMap<>();
    private AlchemyLanguage service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button) findViewById(R.id.submitButton);
        inputText = (EditText) findViewById(R.id.myInputText);
        outputText = (TextView) findViewById(R.id.myResponse);
        service = new AlchemyLanguage();
        service.setApiKey("INSERT API_KEY");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendData();
                Log.i("Hello", "onClick: finished?");
            }
        });

    }

    public void sendData() {
        BackgroundTask bg = new BackgroundTask();
        bg.execute(inputText.toString());
    }

    private class BackgroundTask extends AsyncTask<String, Void, DocumentEmotion> {
        @Override
        protected DocumentEmotion doInBackground(String... params) {
            Map<String, Object> param = new HashMap<>();
            param.put("text", params[0]);
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
            Log.i("PostExecute", "Finished" + result.getEmotion().getAnger());

            String myText = "Anger" + result.getEmotion().getAnger() + "\n" +
                    "Disgust" +  result.getEmotion().getDisgust() + "\n" +
                    "Fear" +  result.getEmotion().getFear() + "\n" +
                    "Joy" + result.getEmotion().getJoy() + "\n" +
                    "Sadness" + result.getEmotion().getSadness();

            Log.i("MyText: ", myText);

            outputText.setText(myText);
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
