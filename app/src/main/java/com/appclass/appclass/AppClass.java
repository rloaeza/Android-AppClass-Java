package com.appclass.appclass;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.appclass.appclass.db.Refs;
import com.appclass.appclass.db.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppClass extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etClave;
    private EditText etIdControl;
    private EditText etNombre;
    private EditText etApellidos;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference databaseReference;

    private String btMac;


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

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH
            }, 1);

            etCorreo = findViewById(R.id.etCorreo);
            etClave = findViewById(R.id.etClave);
            etIdControl = findViewById(R.id.etIdControl);
            etNombre = findViewById(R.id.etNombre);
            etApellidos = findViewById(R.id.etApellidos);


            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference(Refs.AppClass).child(Refs.usuarios);
            btMac = Funciones.getBluetoothMAC(this);


            findViewById(R.id.bIniciarSesion).setOnClickListener(e->{
                if(verificarCorreoClave())
                    iniciarSesion(etCorreo.getText().toString(), etClave.getText().toString());
                else
                    Toast.makeText(this, getString(R.string.iniciarSesionCamposVacios), Toast.LENGTH_SHORT).show();
            });

            findViewById(R.id.bRegistrar).setOnClickListener(e->{
                if(verificarDatosRegistro()) {

                    AlertDialog.Builder builderClave = new AlertDialog.Builder(this);
                    builderClave.setTitle( getString(R.string.verificarClave) );
                    final EditText etClaveVerificar = new EditText(this);
                    etClaveVerificar.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builderClave.setView(etClaveVerificar);
                    builderClave.setPositiveButton(getString(R.string.aceptar), (dialog, which) -> {
                        if(etClave.getText().toString().equals(etClaveVerificar.getText().toString())) {
                            registrar(etCorreo.getText().toString(), etClave.getText().toString());
                        }
                        else {
                            Toast.makeText(this, getString(R.string.iniciarSesionClaveError), Toast.LENGTH_SHORT).show();
                        }
                    });
                    builderClave.setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.cancel());
                    builderClave.show();


                }
                else
                    Toast.makeText(this, getString(R.string.iniciarSesionCamposVacios), Toast.LENGTH_SHORT).show();
            });
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if( user != null ) {
                        iniciarAppClass();
                    }
                }
            };

        }

    }


    private boolean verificarDatosRegistro() {

        if(etIdControl.getVisibility() == View.INVISIBLE){
            Funciones.editTextSVisibility(View.VISIBLE, etIdControl, etNombre, etApellidos);
            etIdControl.requestFocus();
        }

        if(btMac==null) {
            AlertDialog.Builder builderClave = new AlertDialog.Builder(this);
            builderClave.setTitle( getString(R.string.loginBluetoothSolicitar) );
            final EditText etBTMac = new EditText(this);
            etBTMac.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_CLASS_TEXT);
            builderClave.setView(etBTMac);
            builderClave.setPositiveButton(getString(R.string.aceptar), (dialog, which) -> {
                btMac = etBTMac.getText().toString();
            });
            builderClave.setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.cancel());
            builderClave.show();
            return false;

        }
        else {
            btMac = btMac.toUpperCase();
        }

        return  Funciones.verificarEditTexts(etIdControl, etNombre, etApellidos, etCorreo, etClave);
    }
    private boolean verificarCorreoClave() {
        return Funciones.verificarEditTexts(etCorreo, etClave);
    }

    private void iniciarAppClass() {
        Intent intent = new Intent(this, ClaseListado.class);
        startActivity(intent);
    }





    private void crearUsuario() {
        String correo = Funciones.getCorreo();
        String correoFix=Funciones.getCorreoFix(correo);


        databaseReference.child(correoFix).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    String idControl = etIdControl.getText().toString();
                    String nombre = etNombre.getText().toString();
                    String apellidos = etApellidos.getText().toString();

                    databaseReference.child(correoFix).setValue(
                            new Usuario(correo, nombre, apellidos, idControl, btMac, false)
                    );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void registrar(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful()) {
                    crearUsuario();
                    Toast.makeText(AppClass.this, getString(R.string.iniciarSesionRegistrarCorrecto), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AppClass.this, getString(R.string.iniciarSesionErrorRegistrar), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void iniciarSesion(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    iniciarAppClass();
                }
                else {
                    Toast.makeText(AppClass.this, getString(R.string.iniciarSesionErrorIniciar), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        }
    }

}
