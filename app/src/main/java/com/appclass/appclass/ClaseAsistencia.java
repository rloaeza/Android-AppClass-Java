package com.appclass.appclass;

import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appclass.appclass.db.Alumno;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClaseAsistencia extends AppCompatActivity {

    Button bFecha;
    Button bBuscarBT;
    Button bCrearLista;
    Button bTerminar;
    ListView lvAlumnos;
    String claseCodigo;
    String claseNombre;

    AlumnoItemAdapter listaAlumnos;

    String fechaListaAnterior;
    String fechaLista;
    String correo;
    String correoFix;


    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference databaseReference;

    private List<Alumno> listaAlumnosClase;
    private ValueEventListener postListenerCargarListaAsistencia;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_asistencia);

        claseCodigo = getIntent().getStringExtra(AppClassReferencias.claseCodigo);
        claseNombre = getIntent().getStringExtra(AppClassReferencias.claseNombre);

        setTitle(claseNombre);
        bFecha = findViewById(R.id.bFecha);
        bBuscarBT = findViewById(R.id.bBuscarBT);
        bTerminar = findViewById(R.id.bTerminar);
        lvAlumnos = findViewById(R.id.lvAlumnos);
        bCrearLista = findViewById(R.id.bCrearLista);



        correo = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        correoFix=correo.replace(".", "+");


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(AppClassReferencias.AppClass);


        fechaLista = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        fechaListaAnterior = "";
        bFecha.setText(fecha2Text());

        listaAlumnos= new AlumnoItemAdapter(getApplicationContext(), new ArrayList<>(), claseCodigo, databaseReference, correoFix);



        bFecha.setOnClickListener(e->{
            DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    fechaListaAnterior = fechaLista;
                    fechaLista = year + "-" + (month<9?"0":"")+(month+1) + "-" + (day<10?"0":"")+day;
                    bFecha.setText(fecha2Text());
                    cargarLista();
                }
            });
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } );


        lvAlumnos.setAdapter(listaAlumnos);



        bTerminar.setOnClickListener(e -> finish() );




        bCrearLista.setOnClickListener(e-> {




            databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Asistencias).child(fechaLista).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(Alumno alumno : listaAlumnosClase) {
                        alumno.setAsistio("0");
                        //listaAlumnos.add(alumno);

                        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Asistencias).child(fechaLista).child(alumno.getId()).setValue(alumno);
                    }
                    bCrearLista.setVisibility(View.INVISIBLE);
                    bBuscarBT.setVisibility(View.VISIBLE);
                    bTerminar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });





        listaAlumnosClase = new ArrayList<>();

        ValueEventListener postListenerCargarAlumno = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAlumnosClase.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Alumno alumno = item.getValue(Alumno.class);
                    listaAlumnosClase.add(alumno);
                }
                Toast.makeText(ClaseAsistencia.this, ""+listaAlumnosClase.size(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Alumnos).addValueEventListener(postListenerCargarAlumno);


        cargarLista();




        //BT
        BTAdapter = BluetoothAdapter.getDefaultAdapter();


        bBuscarBT.setOnClickListener(e-> {

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
                Log.e("BT", "Iniciando BT -> "+b);
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(broadcastReceiver,filter);
            }
        });

    }

    private void cargarLista() {

        listaAlumnos.setFecha(fechaLista);
        if(!fechaListaAnterior.isEmpty())
            databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Asistencias).child(fechaListaAnterior).removeEventListener(postListenerCargarListaAsistencia);
        postListenerCargarListaAsistencia = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAlumnos.clear();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Alumno alumno = item.getValue(Alumno.class);
                        listaAlumnos.add(alumno);
                    }
                    //Toast.makeText(ClaseAsistencia.this, "" + listaAlumnosClase.size(), Toast.LENGTH_SHORT).show();
                    bCrearLista.setVisibility(View.INVISIBLE);
                    bBuscarBT.setVisibility(View.VISIBLE);
                    bTerminar.setVisibility(View.VISIBLE);
                } else {
                    bCrearLista.setVisibility(View.VISIBLE);
                    bBuscarBT.setVisibility(View.INVISIBLE);
                    bTerminar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Asistencias).child(fechaLista).addValueEventListener(postListenerCargarListaAsistencia);


    }

    private String fecha2Text() {
        Date fecha = Calendar.getInstance().getTime();
        try {
            fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaLista);
        } catch (ParseException e1) { }
        return new SimpleDateFormat("dd / MMM / yyyy").format(fecha);
    }








    // BT

    public static int REQUEST_BLUETOOTH = 1;

    BluetoothAdapter BTAdapter;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            // Log.e("BT", action);
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(bluetoothDevice!=null) {
                    Log.e("BT",  ""+bluetoothDevice.getAddress());
                    Alumno alumno=existeBT(bluetoothDevice.getAddress()+"");
                    if( alumno !=null) {
                        if(alumno.getAsistio().equals("0"))
                          databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Asistencias).child(fechaLista).child(alumno.getId()).child(AppClassReferencias.bdAsistio).setValue(
                                "1"
                        );
                    }
                   /*
                    if(!existeValor(bluetoothDevice.getAddress())) {
                        items.add(bluetoothDevice.getAddress());
                        arrayAdapter.notifyDataSetChanged();
                    }
*/
                }

            }
        }
    };

    private Alumno existeBT(String macBT) {
        for(int indice=0; indice<listaAlumnos.getCount(); indice++) {
            if( listaAlumnos.getItem(indice).getBtMAC().equalsIgnoreCase(macBT) )
                return listaAlumnos.getItem(indice);

        }
        return null;
    }





}
