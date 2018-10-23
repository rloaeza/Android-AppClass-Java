package com.appclass.appclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ClaseItemAdapter extends ArrayAdapter<Clase>{

    public ClaseItemAdapter(Context context, List<Clase> objects) {
        super(context, 0, objects);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.item_clase,
                    parent,
                    false);
        }

        TextView tvNombreClase = convertView.findViewById(R.id.tvNombreClase);
        TextView tvCantidadAlumnos = convertView.findViewById(R.id.tvCantidadAlumnos);
        TextView tvCodigo = convertView.findViewById(R.id.tvCodigo);
        ImageView ivEditar = convertView.findViewById(R.id.ivEditar);
        RelativeLayout rlFondo = convertView.findViewById(R.id.rlFondo);


        if( position%2 == 0)
        {
            rlFondo.setBackgroundResource(R.color.colorFondoListaClase);
        }
        else{
            rlFondo.setBackgroundColor(Color.TRANSPARENT);
        }

        Clase itemClase = getItem(position);


        tvNombreClase.setText( itemClase.getNombre() );
        tvCantidadAlumnos.setText( convertView.getResources().getString(R.string.cantidadAlumnos).replace("#", itemClase.getCantidadAlumnos()+""));
        tvCodigo.setText(itemClase.getCodigo());



        ivEditar.setOnClickListener(e->  {
            Intent intent = new Intent(this.getContext(), ClaseDetalles.class);
            intent.putExtra(AppClassReferencias.claseCodigo, itemClase.getCodigo());
            intent.putExtra(AppClassReferencias.claseNombre, itemClase.getNombre());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);



        });




        return convertView;

    }
}
