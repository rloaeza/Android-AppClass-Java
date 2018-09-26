package com.appclass.appclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ListadoClases extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_listado_clases);
    }

}
