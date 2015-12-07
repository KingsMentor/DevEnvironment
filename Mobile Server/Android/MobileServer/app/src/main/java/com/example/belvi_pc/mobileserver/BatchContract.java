package com.example.belvi_pc.mobileserver;

/**
 * Created by BELVI-PC on 12/6/2015.
 */
public class BatchContract {
    public static final String TABLE_NAME = "PENDING";
    public static final String DATABASE_NAME = "BATCH_TABLE";
    public static final String COLUMN_MESSAGE = "COLUMN_MESSAGE";
    public static final String COLUMN_SENDER_NUMBER = "COLUMN_SENDER_NUMBER";
    public static final String COLUMN_ID = "_id";
    public static final int DATABASE_VERSION = 1;

    public static final String BATCH_PROVIDER = "mobile_server.batch";
    public static final String BATCH_URI = "content://" + BATCH_PROVIDER + "/" + TABLE_NAME;

    public static Batch batch;
}
