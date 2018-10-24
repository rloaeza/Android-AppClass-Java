package com.appclass.appclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import com.appclass.appclass.db.Refs;
import com.appclass.appclass.db.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;


public class AlumnoDetalle extends AppCompatActivity {
    private GraphView graph;
    private TextView tvNombre;
    private TextView tvClase;
    private Button bAceptar;
    String claseCodigo;
    String claseNombre;
    String usuarioNombre;
    String usuarioIdCodigo;
    private FirebaseDatabase firebaseDatabase ;
    private DatabaseReference databaseReference;
    private int asistencias;
    private int faltas;
    private int clasesTotales;

    BarGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_detalle);

        setTitle(R.string.alumnoDetallesAsistencia);

        Intent intent = getIntent();
        claseCodigo  = intent.getStringExtra(Refs.claseCodigo);
        claseNombre  = intent.getStringExtra(Refs.claseNombre);

        usuarioNombre = intent.getStringExtra(Refs.usuarioNombre);
        usuarioIdCodigo = intent.getStringExtra(Refs.usuarioCodigo);


        graph = (GraphView) findViewById(R.id.graph);
        tvClase = findViewById(R.id.tvClase);
        tvNombre = findViewById(R.id.tvNombre);
        bAceptar = findViewById(R.id.bAceptar);


        tvClase.setText(claseNombre);
        tvNombre.setText(usuarioNombre);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Refs.AppClass);

        clasesTotales = 0;
        asistencias = 0;
        faltas = 0;

        databaseReference.child(Refs.asistencia).orderByKey().startAt(claseCodigo).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()) {
                    asistencias=0;
                    faltas=0;
                    clasesTotales = 0;
                    for(DataSnapshot data : dataSnapshot.getChildren()) {


                        if(!data.getKey().startsWith(claseCodigo))
                            continue;

                        clasesTotales++;
                        for(DataSnapshot d: data.getChildren()) {

                            Usuario usuario = d.getValue(Usuario.class);
                            if( !usuario.getIdControl().equals(usuarioIdCodigo) )
                                continue;

                            if(!usuario.isAsistio()) {
                                faltas++;
                            }
                            else{
                                asistencias++;
                            }

                        }

                    }
                    graph.removeAllSeries();

                    series = new BarGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, 0),
                            new DataPoint(1, asistencias),
                            new DataPoint(2, (clasesTotales-(asistencias+faltas) ) + faltas),
                            new DataPoint(3, 0),

                    });

                    series.setSpacing(50);
                    graph.addSeries(series);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, 0),
                new DataPoint(2, 0),
                new DataPoint(3, 0),

        });



        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"", getString(R.string.alumnoAsistencias), getString(R.string.alumnoFaltas), ""});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        series.setSpacing(50);
        graph.addSeries(series);



        bAceptar.setOnClickListener(e -> finish() );


    }
}
