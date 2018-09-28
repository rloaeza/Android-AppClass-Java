package com.appclass.appclass;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ListadoClases extends AppCompatActivity {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Materia");

                final EditText input = new EditText(this);

                input.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lvClasesAdapter.add(new ItemClase(input.getText().toString(), "","",true, 10 + (int)(Math.random()*40)) );

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
