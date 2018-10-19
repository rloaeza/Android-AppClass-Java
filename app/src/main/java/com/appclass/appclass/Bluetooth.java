package com.appclass.appclass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.appclass.appclass.db.Refs;
import com.appclass.appclass.db.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Bluetooth extends AppCompatActivity {


    private ListView lvDispositivos;
    private Button bBuscarBT;
    private Button bTerminar;

    private ArrayList<String> listaBT;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bt);



        lvDispositivos = findViewById(R.id.lvDispositivos);
        bBuscarBT = findViewById(R.id.bBuscarBT);
        bTerminar = findViewById(R.id.bTerminar);

        bTerminar.setOnClickListener( e -> finish() );


        listaBT = new ArrayList<>();


        listAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaBT);
        lvDispositivos.setAdapter(listAdapter);


        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        bBuscarBT.setOnClickListener(e-> {
            listaBT.clear();
            listAdapter.notifyDataSetChanged();

            if (!BTAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            }
            else {

                boolean b = BTAdapter.startDiscovery();
                if(b) {
                    bBuscarBT.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            bBuscarBT.setEnabled(true);
                            BTAdapter.cancelDiscovery();
                        }
                    }, 15000);
                }

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(broadcastReceiver,filter);
            }
        });


    }




    // BT

    public static int REQUEST_BLUETOOTH = 1;

    BluetoothAdapter BTAdapter;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(bluetoothDevice!=null) {

                    if(bluetoothDevice.getName()!=null) {

                        listaBT.add(bluetoothDevice.getAddress() +" -> "+ bluetoothDevice.getName());
                    }
                    else
                        listaBT.add(bluetoothDevice.getAddress());


                    listAdapter.notifyDataSetChanged();

                }

            }
        }
    };
}
