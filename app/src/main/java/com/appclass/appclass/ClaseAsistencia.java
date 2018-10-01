package com.appclass.appclass;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appclass.appclass.db.Alumno;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ClaseAsistencia extends AppCompatActivity {

    Button bFecha;
    Button bBuscarBT;
    Button bCrearLista;
    Button bTerminar;
    ListView lvAlumnos;
    String claseCodigo;
    String claseNombre;

    AlumnoItemAdapter listaAlumnos;


    String fechaLista;
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

        listaAlumnos= new AlumnoItemAdapter(getApplicationContext(), new ArrayList<>());

        String correo = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        String correoFix=correo.replace(".", "+");

        fechaLista = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        bFecha.setText(fecha2Text());

        bFecha.setOnClickListener(e->{
            DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    fechaLista = year + "-" + (month<9?"0":"")+(month+1) + "-" + day;
                    bFecha.setText(fecha2Text());
                }
            });
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } );


        lvAlumnos.setAdapter(listaAlumnos);



        bTerminar.setOnClickListener(e -> finish() );

        if(listaAlumnos.isEmpty()) {
            bCrearLista.setVisibility(View.VISIBLE);
            bBuscarBT.setVisibility(View.INVISIBLE);
            bTerminar.setVisibility(View.INVISIBLE);
        }


        bCrearLista.setOnClickListener(e-> {
            bCrearLista.setVisibility(View.INVISIBLE);
            bBuscarBT.setVisibility(View.VISIBLE);
            bTerminar.setVisibility(View.VISIBLE);
            for(int i=0; i<10; i++) {
                listaAlumnos.add(new Alumno("1", "Roberto", "Loaeza", "Valerio", "MAC", "email", "0"));
                listaAlumnos.add(new Alumno("2", "Iker", "Loaeza", "Gutierrez", "MAC", "email", "1"));
                listaAlumnos.add(new Alumno("3", "Irma", "Gutierrez", "Miranda", "MAC", "email", "0"));

            }

        });

    }

    private String fecha2Text() {
        Date fecha = Calendar.getInstance().getTime();
        try {
            fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaLista);
        } catch (ParseException e1) { }
        return new SimpleDateFormat("dd / MMM / yyyy").format(fecha);
    }
}
