package com.example.belvi_pc.mobileserver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BELVI-PC on 12/3/2015.
 */
public class MessageReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (Utils.isConnected(context)) {
                Cursor cursor = context.getContentResolver().query(Uri.parse(BatchContract.BATCH_URI), new String[]{BatchContract.COLUMN_MESSAGE, BatchContract.COLUMN_SENDER_NUMBER, BatchContract.COLUMN_ID}, null, null, null);
                Utils.sendBatch(context, cursor, BatchConstant.BATCH_LINK);
            }
        } else if (intent.getAction().equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle pudsBundle = intent.getExtras();
            Object[] pdus = (Object[]) pudsBundle.get("pdus");
            SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
            String body = messages.getMessageBody();
            Log.i("TAG", messages.getOriginatingAddress());
            if (Utils.matchesCriteria(messages.getMessageBody())) {
                String sender = messages.getOriginatingAddress();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(BatchConstant.BODY, body);
                hashMap.put(BatchConstant.SENDER, sender);
                Intent internetServiceIntent = new Intent(context, InternetService.class);
                internetServiceIntent.putExtra(BatchConstant.DATA, hashMap);
                internetServiceIntent.putExtra(BatchConstant.TYPE, BatchConstant.SINGLETON);
                internetServiceIntent.putExtra(BatchConstant.LINK, BatchConstant.SINGLETON_LINK);
                context.startService(internetServiceIntent);
                abortBroadcast();
            }
        }

    }

}
