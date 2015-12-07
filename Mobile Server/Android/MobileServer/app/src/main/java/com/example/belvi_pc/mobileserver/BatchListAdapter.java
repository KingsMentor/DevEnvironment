package com.example.belvi_pc.mobileserver;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by BELVI-PC on 12/7/2015.
 */
public class BatchListAdapter extends CursorAdapter {
    public BatchListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public BatchListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.batch_list_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView sender = (TextView) view.findViewById(R.id.sender);
        TextView message = (TextView) view.findViewById(R.id.message);
        sender.setText(cursor.getString(cursor.getColumnIndex(BatchContract.COLUMN_SENDER_NUMBER)));
        message.setText(cursor.getString(cursor.getColumnIndex(BatchContract.COLUMN_MESSAGE)));
    }

}
