package com.example.belvi_pc.mobileserver;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BELVI-PC on 12/7/2015.
 */
public class InternetService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public InternetService() {
        super("");
    }

    public InternetService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String link = intent.getStringExtra(BatchConstant.LINK);
        HashMap<String, String> data = (HashMap<String, String>) intent.getSerializableExtra(BatchConstant.DATA);
        ArrayList<String> pendingIds = intent.getStringArrayListExtra(BatchConstant.PENDING_IDS);

        int type = intent.getIntExtra(BatchConstant.TYPE, -1);
        Log.e("Link", "link: " + link);
        Log.e("type", String.valueOf(type));
        Log.e("data", String.valueOf(data));
        Log.e("pendingIds", String.valueOf(pendingIds));
        if (type == BatchConstant.BATCH) {
            if (pendingIds.size() > 0) {
                if (Utils.internetCall(link, data) == HttpURLConnection.HTTP_OK) {
                    Log.e("Done Batch:", link);
                    String where = "";
                    for (String batchId : pendingIds) {
                        where += " _id = " + batchId + " OR ";
                    }
                    where = where.substring(0, where.lastIndexOf("OR"));
                    getContentResolver().delete(Uri.parse(BatchContract.BATCH_URI), where, null);
                    Utils.broadcastUpdate(this);
                }
            }
        } else if (type == BatchConstant.SINGLETON) {
            if (!(Utils.internetCall(link, data) == HttpURLConnection.HTTP_OK)) {
                Log.e("Single Batch:", link);
                ContentValues contentValues = new ContentValues();
                contentValues.put(BatchContract.COLUMN_MESSAGE, data.get(BatchConstant.BODY));
                contentValues.put(BatchContract.COLUMN_SENDER_NUMBER, data.get(BatchConstant.SENDER));
                getContentResolver().insert(Uri.parse(BatchContract.BATCH_URI), contentValues);
                Utils.broadcastUpdate(this);
            }
        }
    }
}
