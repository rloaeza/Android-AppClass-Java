package com.appclass.appclass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.appclass.appclass.db.Alumno;
import com.appclass.appclass.db.Refs;
import com.appclass.appclass.db.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;

public class AlumnoDatos extends AppCompatActivity {
    EditText etBT;
    EditText etId;
    EditText etNombre;
    EditText etApellidos;

    EditText etCorreo;
    Spinner sBT;
    ImageView ivBuscarBT;

    Button bAceptar;
    Button bCancelar;


    private ArrayList<String> listaBT;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        etId = findViewById(R.id.etId);
        etBT = findViewById(R.id.etBT);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);

        etCorreo = findViewById(R.id.etCorreo);


        bAceptar = findViewById(R.id.bAceptar);
        bCancelar = findViewById(R.id.bCancelar);

        sBT  = findViewById(R.id.sBT);
        ivBuscarBT = findViewById(R.id.ivBuscarBT);


        bAceptar.setOnClickListener( e -> {
            String id = etId.getText().toString();
            String bt = etBT.getText().toString();
            String nombre = etNombre.getText().toString();
            String apellidos = etApellidos.getText().toString();

            String correoAlumno = etCorreo.getText().toString();

            if(!Funciones.verificarEditTexts(etId, etBT, etNombre, etApellidos, etCorreo)) {
                Toast.makeText(this, getString(R.string.alumnoRegistroError), Toast.LENGTH_SHORT).show();
                return;
            }

            String correoFix=Funciones.getCorreoFix(correoAlumno);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference(Refs.AppClass);

            databaseReference.child(Refs.usuarios).child(correoFix).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        Usuario alumno = new Usuario(correoAlumno, nombre, apellidos, id, bt, false);

                        databaseReference.child(Refs.usuarios).child(correoFix).setValue(alumno);
                        limpiarCampos();
                        Toast.makeText(AlumnoDatos.this, getString(R.string.alumnoRegistro), Toast.LENGTH_SHORT).show();


                    }
                    else {
                        Toast.makeText(AlumnoDatos.this, getString(R.string.alumnoRegistroExistente), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        });

        bCancelar.setOnClickListener( e-> finish() );




        listaBT = new ArrayList<>();


        listAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaBT);
        sBT.setAdapter(listAdapter);


        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        ivBuscarBT.setOnClickListener(e-> {
            listaBT.clear();
            listAdapter.notifyDataSetChanged();

            if (!BTAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            }
            else {

                boolean b = BTAdapter.startDiscovery();
                if(b) {
                    ivBuscarBT.setVisibility(View.INVISIBLE);
                    etBT.setText("");
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            ivBuscarBT.setVisibility(View.VISIBLE);
                            BTAdapter.cancelDiscovery();
                        }
                    }, 15000);
                }

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(broadcastReceiver,filter);
            }
        });
        sBT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etBT.setText(listaBT.get(position).substring(0, 17));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void limpiarCampos() {
        etBT.setText("");
        etId.setText("");
        etNombre.setText("");
        etApellidos.setText("");

        etCorreo.setText("");

    }


    public static int REQUEST_BLUETOOTH = 1;

    BluetoothAdapter BTAdapter;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(bluetoothDevice!=null) {

                    if(bluetoothDevice.getName()!=null) {
                        String btName = bluetoothDevice.getName();
                        btName = btName.substring(0, btName.length()>10?10:btName.length());

                        listaBT.add(bluetoothDevice.getAddress() +" ["+ btName+"]");
                    }
                    else
                        listaBT.add(bluetoothDevice.getAddress());


                    listAdapter.notifyDataSetChanged();

                }

            }
        }
    };

}
