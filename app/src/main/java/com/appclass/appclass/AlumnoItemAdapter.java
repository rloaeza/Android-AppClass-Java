package com.appclass.appclass;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.appclass.appclass.db.Alumno;
import com.appclass.appclass.db.Refs;
import com.appclass.appclass.db.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AlumnoItemAdapter extends ArrayAdapter<Usuario>{

    private String claseCodigo;
    private String fecha;

    private DatabaseReference databaseReference;
    private String correoFix;

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public AlumnoItemAdapter(Context context, List<Usuario> objects, String claseCodigo, DatabaseReference databaseReference, String correoFix) {
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

        RelativeLayout rlFondo = convertView.findViewById(R.id.rlFondo);

        if( position%2 == 0)
        {
            rlFondo.setBackgroundResource(R.color.colorFondoListaClase);
        }
        else {
            rlFondo.setBackgroundColor(Color.TRANSPARENT);
        }


        Usuario itemAlumno = getItem(position);

        rbAlumno.setText(itemAlumno.getApellidos()+", "+itemAlumno.getNombre());
        tvId.setText(itemAlumno.getIdControl());

        rbAlumno.setChecked(itemAlumno.isAsistio());

        rbAlumno.setOnClickListener(e-> {
            databaseReference.child(Refs.asistencia).child(claseCodigo+"+"+fecha).child(itemAlumno.getIdControl()).child(Refs.bdAsistio).setValue(             rbAlumno.isChecked()?true:false);
        });

        return convertView;

    }
}
