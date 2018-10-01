package com.appclass.appclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.appclass.appclass.db.Alumno;

import java.util.List;

public class AlumnoItemAdapterDetalles extends ArrayAdapter<Alumno>{

    public AlumnoItemAdapterDetalles(Context context, List<Alumno> objects) {
        super(context, 0, objects);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.item_alumno_detalle,
                    parent,
                    false);
        }


        TextView tvNombre = convertView.findViewById(R.id.tvNombre);


        Alumno itemAlumno = getItem(position);

        tvNombre.setText(itemAlumno.getNombreCompleto());

        return convertView;

    }
}
