package com.example.belvi_pc.mobileserver;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BELVI-PC on 12/7/2015.
 */
public class Utils {

    public static int internetCall(String link, Map<String, String> params) {
        try {
            URL url = new URL(link);
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.setConnectTimeout(3000);
            conn.getOutputStream().write(postDataBytes);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder output = new StringBuilder();
            String line = null;
            while ((line = in.readLine()) != null) {
                output.append(line).append("\n");
            }

            return HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
            return HttpURLConnection.HTTP_INTERNAL_ERROR;
        }
    }

    public static boolean matchesCriteria(String message) {
        return message.startsWith("Dear Subscriber");
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public static void sendBatch(Context context, Cursor cursor, String link) {
        if (cursor != null) {
            ArrayList<String> pendingIds = new ArrayList<>();
            JSONArray object = new JSONArray();
            while (cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                String body = cursor.getString(cursor.getColumnIndex(BatchContract.COLUMN_MESSAGE));
                String sender = cursor.getString(cursor.getColumnIndex(BatchContract.COLUMN_SENDER_NUMBER));
                pendingIds.add(cursor.getString(cursor.getColumnIndex(BatchContract.COLUMN_ID)));
                try {
                    jsonObject.put(BatchConstant.BODY, body);
                    jsonObject.put(BatchConstant.SENDER, sender);
                    object.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            HashMap<String, String> stringMap = new HashMap<>();
            stringMap.put(BatchConstant.BATCH_REQUEST, object.toString());
            Intent intent = new Intent(context, InternetService.class);
            intent.putExtra(BatchConstant.LINK, link);
            intent.putExtra(BatchConstant.DATA, stringMap);
            intent.putExtra(BatchConstant.PENDING_IDS, pendingIds);
            intent.putExtra(BatchConstant.TYPE, BatchConstant.BATCH);
            context.startService(intent);

        }
    }

    public static void broadcastUpdate(Context context) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.sendBroadcast(new Intent(BatchConstant.UPDATE_BROADCAST));
    }
}
