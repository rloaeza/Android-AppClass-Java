package com.appclass.appclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.appclass.appclass.db.Alumno;

import java.util.List;

public class AlumnoItemAdapter extends ArrayAdapter<Alumno>{

    public AlumnoItemAdapter(Context context, List<Alumno> objects) {
        super(context, 0, objects);


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

        return convertView;

    }
}
