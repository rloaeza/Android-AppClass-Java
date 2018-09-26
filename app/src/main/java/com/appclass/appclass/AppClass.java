package com.appclass.appclass;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AppClass extends AppCompatActivity {

    private String pinCodeRegistrado= "5555";

    private String pinCode ;

    private int longitudPinCode = 4;

    private ProgressBar pbPinCode;
    private String btMacLocal;
    private Button bAyuda;
    private TextView ayudaPinCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getSupportActionBar().hide();
        setContentView(R.layout.activity_app_class);


        BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
        if(BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.noCompatible))
                    .setMessage(getString(R.string.noBluetooth))
                    .setPositiveButton(getString(R.string.salir), (dialog, which) -> System.exit(0))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else {
            if (!BTAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, 1);
            }


            pbPinCode = findViewById(R.id.pbPinCode);
            pbPinCode.setMax(longitudPinCode);
            bAyuda = findViewById(R.id.bAyuda);
            ayudaPinCode = findViewById(R.id.ayudaPinCode);

            limpiarPinCode();
            findViewById(R.id.b0).setOnClickListener(e -> agregarPinCode("0"));
            findViewById(R.id.b1).setOnClickListener(e -> agregarPinCode("1"));
            findViewById(R.id.b2).setOnClickListener(e -> agregarPinCode("2"));
            findViewById(R.id.b3).setOnClickListener(e -> agregarPinCode("3"));
            findViewById(R.id.b4).setOnClickListener(e -> agregarPinCode("4"));
            findViewById(R.id.b5).setOnClickListener(e -> agregarPinCode("5"));
            findViewById(R.id.b6).setOnClickListener(e -> agregarPinCode("6"));
            findViewById(R.id.b7).setOnClickListener(e -> agregarPinCode("7"));
            findViewById(R.id.b8).setOnClickListener(e -> agregarPinCode("8"));
            findViewById(R.id.b9).setOnClickListener(e -> agregarPinCode("9"));
            findViewById(R.id.bX).setOnClickListener(e -> limpiarPinCode());

            findViewById(R.id.bAyuda).setOnClickListener(e -> {
                ayudaPinCode.setText(getString(R.string.ayudaLogin).replace("MAC_BT", btMacLocal));
                ayudaPinCode.setVisibility(View.VISIBLE);
            });


            btMacLocal = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
            Log.e("BT MAC", btMacLocal);
        }
    }


    private void agregarPinCode(String code) {
        if(ayudaPinCode.getVisibility()==View.VISIBLE)
            ayudaPinCode.setVisibility(View.INVISIBLE);
        pinCode += code;
        actualizarPBPincode();
    }
    private void limpiarPinCode() {
        pinCode= "";
        actualizarPBPincode();
    }
    private void actualizarPBPincode() {
        pbPinCode.setProgress(pinCode.length());
        if(pinCode.length() == longitudPinCode) {
            if(validarPinCode()) {
                Intent intent = new Intent(this, ListadoClases.class);
                startActivity(intent);
                limpiarPinCode();
            }
            else {
                limpiarPinCode();
                bAyuda.setText(getString(R.string.ayudaLoginLargo));
                bAyuda.setTextColor(Color.RED);
            }
        }

    }

    private boolean validarPinCode() {
        if(pinCode.equals(pinCodeRegistrado) )
            return true;
        return false;
    }
}
