package com.example.belvi_pc.mobileserver;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by BELVI-PC on 12/6/2015.
 */
public class BatchHelper {

    public BatchHelper() {

    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "
                + BatchContract.TABLE_NAME + "( "
                + BatchContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "" + BatchContract.COLUMN_MESSAGE + " VARCHAR (50), " +
                " " + BatchContract.COLUMN_SENDER_NUMBER + " VARCHAR(50) );";
        db.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
