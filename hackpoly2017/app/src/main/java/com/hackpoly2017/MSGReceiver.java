package com.hackpoly2017;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Gary on 2/11/2017.
 */
public class MSGReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){

            Toast.makeText(context,"Message received",Toast.LENGTH_LONG).show();

            Object[] pdus = (Object[]) bundle.get("pdus");


            for (int i = 0; i<pdus.length; i++){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String incomingNumber = smsMessage.getOriginatingAddress();
                String message = smsMessage.getMessageBody();
                Toast.makeText(context,"SMS From "+incomingNumber+", Message: "+message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
