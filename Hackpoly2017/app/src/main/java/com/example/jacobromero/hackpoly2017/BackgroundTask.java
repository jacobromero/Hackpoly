package com.example.jacobromero.hackpoly2017;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.AlchemyGenericModel;
import com.ibm.watson.developer_cloud.alchemy.v1.model.AlchemyLanguageGenericModel;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentEmotion;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jacobromero on 2/11/17.
 */

public class BackgroundTask extends AsyncTask<Object, Void, Object>{
    private HashMap<String, Integer> kV = new HashMap<>();

    public BackgroundTask() {
        kV.put("Anger", 0x1F620);
        kV.put("Fear", 0x1F628);
        kV.put("Disgust", 0x1F631);
        kV.put("Joy", 0x1F603);
        kV.put("Sadness", 0x1F61E);
        kV.put("Neutral", 0x1F610);
    }

    AlchemyLanguage service = new AlchemyLanguage();

    private HashMap<String, Double> emotions = new HashMap<>();

    @Override
    protected Object doInBackground(Object... params) {
        service.setApiKey("511873582d6e1c27d0ed73ca26ea2f03c759bd48");
        Map<String, Object> param = new HashMap<>();
        param.put("text", params[0].toString());
        Log.i("Text", "doInBackground: " + params[0].toString());
        DocumentEmotion emotion = service.getEmotion(param).execute();

        Object[] results = new Object[2];
        results[0] = emotion;
        results[1] = params[1];


        return results;
    }

    @Override
    protected void onPostExecute(Object result) {
        Object[] results = (Object[]) result;
        emotions.put("Anger", ((DocumentEmotion) results[0]).getEmotion().getAnger());
        emotions.put("Disgust", ((DocumentEmotion) results[0]).getEmotion().getDisgust());
        emotions.put("Fear", ((DocumentEmotion) results[0]).getEmotion().getFear());
        emotions.put("Joy", ((DocumentEmotion) results[0]).getEmotion().getJoy());
        emotions.put("Sadness", ((DocumentEmotion) results[0]).getEmotion().getSadness());
        emotions.put("Neutral", 0.0);
        Log.i("PostExecute", "Finished" + ((DocumentEmotion) results[0]).getEmotion().getAnger());

        String myText = "Anger" + ((DocumentEmotion) results[0]).getEmotion().getAnger() + "\n" +
                "Disgust" +  ((DocumentEmotion) results[0]).getEmotion().getDisgust() + "\n" +
                "Fear" +  ((DocumentEmotion) results[0]).getEmotion().getFear() + "\n" +
                "Joy" + ((DocumentEmotion) results[0]).getEmotion().getJoy() + "\n" +
                "Sadness" + ((DocumentEmotion) results[0]).getEmotion().getSadness();

        Log.i("MyText ", myText);

        Object[] keys = emotions.keySet().toArray();
        String largest = "Neutral";
        for(Object k : keys){
            if (emotions.get((String)k) > emotions.get(largest)){
                largest = (String)k;
            }
        }

        Emoji(largest, emotions.get(largest), (TextView) results[1]);

    }

    @Override
    protected void onProgressUpdate(Void... values) {}

    private void Emoji(String text, Double percent, TextView outputText) {
        int unicode = kV.get(text);
        String emoji = new String(Character.toChars(unicode));
        outputText.setText(percent + " " + emoji);
        outputText.setTextSize(100);
    }
}
