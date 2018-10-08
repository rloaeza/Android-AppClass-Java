package com.appclass.appclass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Switch;

public class Configuracion extends AppCompatActivity  {

    Button bAceptar;
    Switch sSepararAlumnos;
    Switch sPermitirModificarAsistenciasPasadas;
    Switch sOrdenarClasesNombre;
    Switch sNoSolicitarConfirmacion;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        bAceptar = findViewById(R.id.bAceptar);

        bAceptar.setOnClickListener(e->finish());
    }
}
