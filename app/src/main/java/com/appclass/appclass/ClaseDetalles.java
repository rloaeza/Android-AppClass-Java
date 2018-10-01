package com.appclass.appclass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.appclass.appclass.db.Alumno;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClaseDetalles extends AppCompatActivity {
    private Button bNombre;
    private ListView lvAlumnos;
    private ImageView bAgregar;
    private String claseCodigo;
    private String claseNombre;
    private Spinner sAlumnos;
    private ImageView ivBorrarClase;
    private ImageView ivLimpiarClase;


    private AlumnoItemAdapterDetalles listaAlumnosDetalles;
    private List<Alumno> listaAlumnos;


    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference databaseReference;

    ArrayAdapter<String> adapterSpinner;

    Clase claseActual;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase_detalles);
        setTitle(getString(R.string.claseDetalles));
        bNombre = findViewById(R.id.bNombre);
        lvAlumnos = findViewById(R.id.lvAlumnos);
        bAgregar = findViewById(R.id.bAgregar);
        sAlumnos = findViewById(R.id.sAlumnos);
        ivBorrarClase = findViewById(R.id.ivBorrarClase);
        ivLimpiarClase = findViewById(R.id.ivLimpiarClase);

        claseCodigo = getIntent().getStringExtra(AppClassReferencias.claseCodigo);
        claseNombre = getIntent().getStringExtra(AppClassReferencias.claseNombre);
        bNombre.setText(claseNombre);

        listaAlumnosDetalles= new AlumnoItemAdapterDetalles(getApplicationContext(), new ArrayList<>());
        listaAlumnos = new ArrayList<>();


        String correo = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        String correoFix=correo.replace(".", "+");

        adapterSpinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaAlumnos);
        sAlumnos.setAdapter(adapterSpinner);


        ValueEventListener postListenerClase = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAlumnosDetalles.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Alumno alumno= item.getValue(Alumno.class);

                    listaAlumnosDetalles.add(alumno);
                }


                databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseActual.getCodigo()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            claseActual.setCantidadAlumnos(listaAlumnosDetalles.getCount());
                            databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseActual.getCodigo()).child(AppClassReferencias.bdCantidadAlumnos).setValue(
                                    claseActual.getCantidadAlumnos()
                            );

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        lvAlumnos.setAdapter(listaAlumnosDetalles);


        ValueEventListener postListenerAlumno = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAlumnos.clear();

                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Alumno alumno= item.getValue(Alumno.class);

                    listaAlumnos.add(alumno);
                }
                adapterSpinner.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };








        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(AppClassReferencias.AppClass);


        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                claseActual = dataSnapshot.getValue(Clase.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Alumnos).addValueEventListener(postListenerAlumno);


        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases)
                .child(claseCodigo).child(AppClassReferencias.Alumnos).addValueEventListener(postListenerClase);

        bAgregar.setOnClickListener( e -> {
            Alumno alumno = listaAlumnos.get(sAlumnos.getSelectedItemPosition());
            databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).
            child(AppClassReferencias.Alumnos).child(alumno.getId()).setValue(alumno);
        });




        bNombre.setOnClickListener(e->{

            AlertDialog.Builder builderClaseNueva = new AlertDialog.Builder(this);
            builderClaseNueva.setTitle(getString(R.string.nombreClase));
            final EditText etNombreClaseNueva = new EditText(this);
            etNombreClaseNueva.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            builderClaseNueva.setView(etNombreClaseNueva);
            builderClaseNueva.setPositiveButton(getString(R.string.aceptar), (dialog, which) -> {
                        String clase = etNombreClaseNueva.getText().toString();

                        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseActual.getCodigo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    claseActual.setNombreClase(etNombreClaseNueva.getText().toString());
                                    databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseActual.getCodigo()).child(AppClassReferencias.bdNombreClase).setValue(
                                            claseActual.getNombreClase()
                                    );
                                    bNombre.setText(claseActual.getNombreClase());
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });



                    }

            );

            builderClaseNueva.setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.cancel());
            builderClaseNueva.show();




        } );


        ivBorrarClase.setOnClickListener( e-> {

            AlertDialog.Builder builderClaseNueva = new AlertDialog.Builder(this);
            builderClaseNueva.setTitle(getString(R.string.claseConfirmarBorrarMSG).replace("#", getString(R.string.claseConfirmarCodigo)));

            final EditText etNombreClaseNueva = new EditText(this);
            etNombreClaseNueva.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            builderClaseNueva.setView(etNombreClaseNueva);
            builderClaseNueva.setPositiveButton(getString(R.string.aceptar), (dialog, which) -> {
                        String msg = etNombreClaseNueva.getText().toString();

                        if(!msg.equals(getString(R.string.claseConfirmarCodigo)))
                            return;
                        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseActual.getCodigo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {

                                    databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseActual.getCodigo()).removeValue();
                                    finish();
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });



                    }

            );

            builderClaseNueva.setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.cancel());
            builderClaseNueva.show();

        });

        ivLimpiarClase.setOnClickListener(e->{

            AlertDialog.Builder builderClaseNueva = new AlertDialog.Builder(this);
            builderClaseNueva.setTitle(getString(R.string.claseConfirmarLimpiarMSG).replace("#", getString(R.string.claseConfirmarCodigo)));

            final EditText etNombreClaseNueva = new EditText(this);
            etNombreClaseNueva.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            builderClaseNueva.setView(etNombreClaseNueva);
            builderClaseNueva.setPositiveButton(getString(R.string.aceptar), (dialog, which) -> {
                        String msg = etNombreClaseNueva.getText().toString();

                        if(!msg.equals(getString(R.string.claseConfirmarCodigo)))
                            return;
                        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseActual.getCodigo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {

                                    databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseActual.getCodigo()).child(AppClassReferencias.Alumnos).removeValue();

                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });



                    }

            );

            builderClaseNueva.setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.cancel());
            builderClaseNueva.show();


        });


    }
}
