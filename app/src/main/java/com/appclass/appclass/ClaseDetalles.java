package com.appclass.appclass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
    private List<Usuario> listaAlumnos;


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

        claseCodigo = getIntent().getStringExtra(Refs.claseCodigo);
        claseNombre = getIntent().getStringExtra(Refs.claseNombre);
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
                    Usuario alumno= item.getValue(Usuario.class);

                    listaAlumnosDetalles.add(alumno);
                }


                databaseReference.child(Refs.usuarios).child(correoFix).child(Refs.clasesPropias).child(claseActual.getCodigo()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            claseActual.setCantidadAlumnos(listaAlumnosDetalles.getCount());
                            databaseReference.child(Refs.usuarios).child(correoFix).child(Refs.clasesPropias).child(claseActual.getCodigo()).child(Refs.bdCantidadAlumnos).setValue(
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
                    Usuario alumno= item.getValue(Usuario.class);

                    listaAlumnos.add(alumno);
                }
                adapterSpinner.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };








        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Refs.AppClass);


        databaseReference.child(Refs.usuarios).child(correoFix).child(Refs.clasesPropias).child(claseCodigo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                claseActual = dataSnapshot.getValue(Clase.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child(Refs.usuarios).orderByChild(Refs.bdUsuarioIdControl).addValueEventListener(postListenerAlumno);


        databaseReference.child(Refs.clases).child(claseCodigo).child(Refs.alumnos).orderByChild(Refs.bdUsuarioIdControl).addValueEventListener(postListenerClase);

        bAgregar.setOnClickListener( e -> {
            Usuario alumno = listaAlumnos.get(sAlumnos.getSelectedItemPosition());
            databaseReference.child(Refs.clases).child(claseCodigo).child(Refs.alumnos).child(Funciones.getCorreoFix(alumno.getCorreo())).setValue(alumno);
            databaseReference.child(Refs.usuarios).child(Funciones.getCorreoFix(alumno.getCorreo())).child(Refs.clases).child(claseActual.getCodigo()).setValue(claseActual);
        });


        lvAlumnos.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Usuario alumno = listaAlumnosDetalles.getItem(i);


            AlertDialog.Builder builderClaseNueva = new AlertDialog.Builder(this);
            builderClaseNueva.setTitle(
                    getString(R.string.claseDetalleBorrarAlumno).replace("#", getString(R.string.confirmarCodigo)).replace("$", alumno.getNombreCompleto())

            );

            final EditText etConfirmacion = new EditText(this);
            etConfirmacion.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            builderClaseNueva.setView(etConfirmacion);
            builderClaseNueva.setPositiveButton(getString(R.string.aceptar), (dialog, which) -> {
                String msg = etConfirmacion.getText().toString();

                if(!msg.equals(getString(R.string.confirmarCodigo)))
                    return;

                databaseReference.child(Refs.clases).child(claseActual.getCodigo()).child(Refs.alumnos).child(Funciones.getCorreoFix(alumno.getCorreo())).removeValue();
                databaseReference.child(Refs.usuarios).child(Funciones.getCorreoFix(alumno.getCorreo())).child(Refs.clases).child(claseActual.getCodigo()).removeValue();

            });

            builderClaseNueva.setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.cancel());
            builderClaseNueva.show();

            return true;
        });



        bNombre.setOnClickListener(e->{

            AlertDialog.Builder builderClaseNueva = new AlertDialog.Builder(this);
            builderClaseNueva.setTitle(getString(R.string.nombreClase));
            final EditText etNombreClaseNueva = new EditText(this);
            etNombreClaseNueva.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
            builderClaseNueva.setView(etNombreClaseNueva);
            builderClaseNueva.setPositiveButton(getString(R.string.aceptar), (dialog, which) -> {
                        String clase = etNombreClaseNueva.getText().toString();

                        databaseReference.child(Refs.usuarios).child(correoFix).child(Refs.clasesPropias).child(claseActual.getCodigo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    claseActual.setNombre(etNombreClaseNueva.getText().toString());
                                    //databaseReference.child(Refs.usuarios).child(correoFix).child(Refs.clasesPropias).child(claseActual.getCodigo()).child(Refs.bdClaseNombre).setValue(claseActual.getNombre());
                                    //databaseReference.child(Refs.clases).child(claseActual.getCodigo()).child(Refs.bdClaseNombre).setValue(claseActual.getNombre());
                                    actualizarNombreClase(claseActual, listaAlumnos);
                                    bNombre.setText(claseActual.getNombre());
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
                        databaseReference.child(Refs.usuarios).child(correoFix).child(Refs.clasesPropias).child(claseActual.getCodigo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    eliminarAlumnosClase(claseActual, listaAlumnos);
                                    databaseReference.child(Refs.usuarios).child(correoFix).child(Refs.clasesPropias).child(claseActual.getCodigo()).removeValue();
                                    databaseReference.child(Refs.clases).child(claseActual.getCodigo()).removeValue();
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
                        databaseReference.child(Refs.usuarios).child(correoFix).child(Refs.clasesPropias).child(claseActual.getCodigo()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    eliminarAlumnosClase(claseActual, listaAlumnos);
                                    databaseReference.child(Refs.clases).child(claseActual.getCodigo()).child(Refs.alumnos).removeValue();

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



    private void eliminarAlumnosClase(Clase clase, List<Usuario> alumnos) {
        for(Usuario alumno : alumnos) {
            databaseReference.child(Refs.usuarios).child(Funciones.getCorreoFix(alumno.getCorreo())).child(Refs.clases).child(clase.getCodigo()).removeValue();
        }
    }


    private void actualizarNombreClase(Clase clase, List<Usuario> alumnos) {

        // cambia el nombre en mis clases propias
        databaseReference.child(Refs.usuarios).child(Funciones.getCorreoFix(clase.getCorreo())).child(Refs.clasesPropias).child(claseActual.getCodigo()).child(Refs.bdClaseNombre).setValue(claseActual.getNombre());
        // cambia el nombre en clases (generales)
        databaseReference.child(Refs.clases).child(claseActual.getCodigo()).child(Refs.bdClaseNombre).setValue(claseActual.getNombre());
        // cambia el nombre de la clase para cada alumno inscrito
        for(Usuario alumno: alumnos) {
            databaseReference.child(Refs.usuarios).child(Funciones.getCorreoFix(alumno.getCorreo())).child(Refs.clases).child(claseActual.getCodigo()).child(Refs.bdClaseNombre).setValue(claseActual.getNombre());
        }
    }




}
