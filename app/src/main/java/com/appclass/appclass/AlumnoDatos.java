package com.appclass.appclass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appclass.appclass.db.Alumno;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlumnoDatos extends AppCompatActivity {
    EditText etBT;
    EditText etId;
    EditText etNombre;
    EditText etApaterno;
    EditText etAmaterno;
    EditText etCorreo;

    Button bAceptar;
    Button bCancelar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        etId = findViewById(R.id.etId);
        etBT = findViewById(R.id.etBT);
        etNombre = findViewById(R.id.etNombre);
        etApaterno = findViewById(R.id.etApaterno);
        etAmaterno = findViewById(R.id.etAmaterno);
        etCorreo = findViewById(R.id.etCorreo);


        bAceptar = findViewById(R.id.bAceptar);
        bCancelar = findViewById(R.id.bCancelar);

        bAceptar.setOnClickListener( e -> {
            String id = etId.getText().toString();
            String bt = etBT.getText().toString();
            String nombre = etNombre.getText().toString();
            String aPaterno = etApaterno.getText().toString();
            String aMaterno = etAmaterno.getText().toString();
            String correoAlumno = etCorreo.getText().toString();

            if(id.isEmpty()) {
                Toast.makeText(this, getString(R.string.alumnoRegistroError), Toast.LENGTH_SHORT).show();
                return;
            }

            String correo = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
            String correoFix=correo.replace(".", "+");

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference(AppClassReferencias.AppClass);
            databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Alumnos).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        Alumno alumno = new Alumno(id, nombre, aPaterno, aMaterno, bt, correoAlumno, "0");

                        databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Alumnos).child(id).setValue(
                                alumno
                        );
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

    }

    private void limpiarCampos() {
        etBT.setText("");
        etId.setText("");
        etNombre.setText("");
        etApaterno.setText("");
        etAmaterno.setText("");
        etCorreo.setText("");

    }

}
