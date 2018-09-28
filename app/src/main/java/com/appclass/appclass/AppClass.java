package com.appclass.appclass;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppClass extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etClave;
    private String btMacLocal;

    private FirebaseAuth.AuthStateListener authStateListener;



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


            etCorreo = findViewById(R.id.etCorreo);
            etClave = findViewById(R.id.etClave);
            btMacLocal = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
            Log.e("BT MAC", btMacLocal);

            findViewById(R.id.bIniciarSesion).setOnClickListener(e->{
                if(verificarCorreoClave())
                    iniciarSesion(etCorreo.getText().toString(), etClave.getText().toString());
                else
                    Toast.makeText(this, getString(R.string.iniciarSesionCamposVacios), Toast.LENGTH_SHORT).show();
            });

            findViewById(R.id.bRegistrar).setOnClickListener(e->{
                if(verificarCorreoClave()) {

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

    private boolean verificarCorreoClave() {
        if(etCorreo.getText().toString().isEmpty())
            return false;
        if(etClave.getText().toString().isEmpty())
            return false;
        return true;
    }

    private void iniciarAppClass() {
        Intent intent = new Intent(this, ListadoClases.class);
        startActivity(intent);
    }
    private void registrar(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful()) {
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
