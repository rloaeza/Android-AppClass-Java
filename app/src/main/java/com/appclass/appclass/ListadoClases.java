package com.appclass.appclass;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.appclass.appclass.db.Persona;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListadoClases extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    boolean existeUsuario = false;

    private ItemClaseAdapter lvClasesAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_listado_clases);

        setTitle(R.string.listadoClases);
        ListView lvClases = findViewById(R.id.lvClases);
        lvClasesAdapter= new ItemClaseAdapter(getApplicationContext(), Clases.getInstance().getClases());

        lvClases.setAdapter(lvClasesAdapter);
        lvClases.setOnItemClickListener((adapterView, view, i, l) -> Toast.makeText(ListadoClases.this, "Click at "+i, Toast.LENGTH_SHORT).show());

        String btMacLocal = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(FireBaseReferences.AppClass);





        databaseReference.child(FireBaseReferences.Personas).child(btMacLocal).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    databaseReference.child(FireBaseReferences.Personas).child(btMacLocal).setValue(
                            new Persona("","", "", btMacLocal, "")
                    );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_clases, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menuBorrarTodo:
                lvClasesAdapter.clear();
                break;
            case R.id.menuAgregarClase:

                AlertDialog.Builder builderClaseNueva = new AlertDialog.Builder(this);
                builderClaseNueva.setTitle( getString(R.string.nombreClase) );
                final EditText etNombreClaseNueva = new EditText(this);
                etNombreClaseNueva.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                builderClaseNueva.setView(etNombreClaseNueva);
                builderClaseNueva.setPositiveButton(getString(R.string.aceptar), (dialog, which) -> lvClasesAdapter.add(new ItemClase(etNombreClaseNueva.getText().toString(), "","",true, 10 + (int)(Math.random()*40)) ));
                builderClaseNueva.setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.cancel());
                builderClaseNueva.show();
                
                break;
            case R.id.menuCerrarSesion:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.salirBackDoble), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
