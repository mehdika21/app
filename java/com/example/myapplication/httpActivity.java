package com.example.myapplication;

import android.content.Intent;
import android.content.Context;

import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ConnectivityChangeListener;
import com.example.myapplication.NetworkChangeReceiver;
import com.example.myapplication.NetworkService;
import com.example.myapplication.R;

import java.util.Map;

public abstract class httpActivity extends AppCompatActivity implements ConnectivityChangeListener {
    // Some attribute..
    NetworkService networkService;
    boolean mBound = false;
    private NetworkChangeReceiver networkReceiver;
    protected String ipadress="http://192.168.176.208";
    private ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        networkReceiver = NetworkChangeReceiver.getInstance(this);

        registerReceiver(networkReceiver, filter);
    }

    @Override
    public void onConnectivityChange(boolean isConnected) {
        if (isConnected) {
            // do something when the conenctivity is restored
        } else {
            // do something when the connectivity is lost
        }
    }

    protected void send(Map<String, String> params) {
        if(!networkReceiver.isConnected()) {
            Toast.makeText(HttpActivity.this,
                    getResources().getString(R.string.verify_your_Internet), Toast.LENGTH_SHORT).show();
            return;
        }

        // More code..
    }

    @Override
    public void onDestroy() {
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
            networkReceiver = null;
        }
        super.onDestroy();
    }
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
    public void onStop(){
        super.onStop();
        unbindService(connection);
        Intent intent = new Intent(this, NetworkService.class);
        stopService(intent);
    }

}



