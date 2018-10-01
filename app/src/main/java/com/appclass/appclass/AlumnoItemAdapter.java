package com.appclass.appclass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;


import com.appclass.appclass.db.Alumno;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AlumnoItemAdapter extends ArrayAdapter<Alumno>{

    private String claseCodigo;
    private String fecha;

    private DatabaseReference databaseReference;
    private String correoFix;

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public AlumnoItemAdapter(Context context, List<Alumno> objects, String claseCodigo, DatabaseReference databaseReference, String correoFix) {
        super(context, 0, objects);
        this.claseCodigo = claseCodigo;
        this.databaseReference = databaseReference;
        this.correoFix = correoFix;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.item_alumno,
                    parent,
                    false);
        }

        CheckBox rbAlumno = convertView.findViewById(R.id.cbAlumno);
        TextView tvId = convertView.findViewById(R.id.tvId);


        Alumno itemAlumno = getItem(position);

        rbAlumno.setText(itemAlumno.getNombreCompleto());
        tvId.setText(itemAlumno.getId());

        rbAlumno.setChecked(itemAlumno.getAsistio().equals("1"));

        rbAlumno.setOnClickListener(e-> {
            databaseReference.child(AppClassReferencias.Personas).child(correoFix).child(AppClassReferencias.Clases).child(claseCodigo).child(AppClassReferencias.Asistencias).child(fecha).child(itemAlumno.getId()).child(AppClassReferencias.bdAsistio).setValue(
                    rbAlumno.isChecked()?"1":"0"
            );




        });

        return convertView;

    }
}
