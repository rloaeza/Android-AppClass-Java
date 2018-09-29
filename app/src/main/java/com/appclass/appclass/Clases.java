package com.appclass.appclass;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Clases {
    public static ArrayList<ItemClase> clases = new ArrayList<>();
    public static Clases  instance = new Clases();


    public  Clases() {



    }

    public static Clases getInstance() { return instance; }
    public List<ItemClase> getClases() {

        return clases;
    }
}
