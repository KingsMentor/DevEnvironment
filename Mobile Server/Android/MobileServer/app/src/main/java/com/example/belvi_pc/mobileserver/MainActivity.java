package com.example.belvi_pc.mobileserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCursor();
                listAdapter.swapCursor(cursor);
                listAdapter.notifyDataSetChanged();
                Utils.sendBatch(MainActivity.this, cursor, BatchConstant.BATCH_LINK);
                Snackbar.make(view, "Sending Batch", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView listView = (ListView) findViewById(R.id.batch_list);
        refreshCursor();
        listAdapter = new BatchListAdapter(this, cursor, false);
        listView.setAdapter(listAdapter);
    }

    private void refreshCursor() {
        if (cursor != null) {
            cursor.close();
        }
        cursor = getContentResolver().query(Uri.parse(BatchContract.BATCH_URI), new String[]{BatchContract.COLUMN_MESSAGE, BatchContract.COLUMN_SENDER_NUMBER, BatchContract.COLUMN_ID}, null, null, null);
    }

    BatchListAdapter listAdapter;


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshCursor();
            listAdapter.swapCursor(cursor);
            listAdapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(BatchConstant.UPDATE_BROADCAST));
        refreshCursor();
        listAdapter.swapCursor(cursor);
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
