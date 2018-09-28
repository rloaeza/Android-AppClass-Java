package com.appclass.appclass;

import java.util.ArrayList;
import java.util.List;

public class Clases {
    public static ArrayList<ItemClase> clases = new ArrayList<>();
    public static Clases  instance = new Clases();

    public  Clases() {

        clases.add(new ItemClase("Matemáticas", "Matemáticas descripcion", "alf", false, 23));
        clases.add(new ItemClase("Química", "Matemáticas descripcion", "alf", false, 22));
        clases.add(new ItemClase("CyERD", "Matemáticas descripcion", "alf", false, 15));
        clases.add(new ItemClase("Redes de computadoras", "Matemáticas descripcion", "alf", false, 45));
        clases.add(new ItemClase("Algebra lineal", "Matemáticas descripcion", "alf", false, 14));
        clases.add(new ItemClase("Contabilidad", "Matemáticas descripcion", "alf", false, 22));


    }

    public static Clases getInstance() {
        return instance;
    }
    public List<ItemClase> getClases() {
        return clases;
    }
}
