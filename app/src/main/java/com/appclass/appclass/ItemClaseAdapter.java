package com.appclass.appclass;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ItemClaseAdapter extends ArrayAdapter<ItemClase>{

    public ItemClaseAdapter(Context context, List<ItemClase> objects) {
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
        ImageView ivEditar = convertView.findViewById(R.id.ivEditar);

        ItemClase itemClase = getItem(position);


        tvNombreClase.setText( itemClase.getNombreClase() );
        tvCantidadAlumnos.setText( convertView.getResources().getString(R.string.cantidadAlumnos).replace("#", itemClase.getCantidadAlumnos()+""));

        View finalConvertView = convertView;
        ivEditar.setOnClickListener(e-> Toast.makeText(finalConvertView.getContext(), "Clickme on "+position, Toast.LENGTH_SHORT).show());




        return convertView;

    }
}