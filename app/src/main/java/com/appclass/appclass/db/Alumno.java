package com.appclass.appclass.db;

public class Alumno {
    private String id;
    private String nombre;
    private String apaterno;
    private String amaterno;
    private String btMAC;
    private String correo;

    public Alumno() {
    }

    public Alumno(String id, String nombre, String apaterno, String amaterno, String btMAC, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.btMAC = btMAC;
        this.correo = correo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApaterno() {
        return apaterno;
    }

    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }

    public String getAmaterno() {
        return amaterno;
    }

    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }

    public String getBtMAC() {
        return btMAC;
    }

    public void setBtMAC(String btMAC) {
        this.btMAC = btMAC;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
