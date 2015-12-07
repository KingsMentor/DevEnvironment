package com.example.belvi_pc.mobileserver;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by BELVI-PC on 12/6/2015.
 */
public class BatchProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        BatchContract.batch = new Batch(getContext(), BatchContract.DATABASE_NAME, null, BatchContract.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (checkColumns(projection)) {
            SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
            sqLiteQueryBuilder.setTables(BatchContract.TABLE_NAME);

            SQLiteDatabase sqLiteDatabase = BatchContract.batch.getWritableDatabase();
            Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowsInserted = BatchContract.batch.getWritableDatabase().insert(BatchContract.TABLE_NAME, null, values);
        Uri uri_ = ContentUris.withAppendedId(Uri.parse(BatchContract.BATCH_URI), rowsInserted);
        getContext().getContentResolver().notifyChange(uri_, null);
        return uri_;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = BatchContract.batch.getWritableDatabase().delete(BatchContract.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = BatchContract.batch.getWritableDatabase().update(BatchContract.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private boolean checkColumns(String[] projections) {
        if (projections != null) {
            String available[] = {BatchContract.COLUMN_MESSAGE, BatchContract.COLUMN_SENDER_NUMBER, BatchContract.COLUMN_ID};
            HashSet<String> requestedColumn = new HashSet<String>(Arrays.asList(projections));
            HashSet<String> availableColumn = new HashSet<String>(Arrays.asList(available));
            return availableColumn.containsAll(requestedColumn);
        }
        return false;
    }
}
