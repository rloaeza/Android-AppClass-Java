package com.appclass.appclass.db;

public class Alumno {
    private String id;
    private String nombre;
    private String apaterno;
    private String amaterno;
    private String btMAC;
    private String correo;
    private String Asistio;

    public Alumno() {
    }

    public Alumno(String id, String nombre, String apaterno, String amaterno, String btMAC, String correo, String asistio) {
        this.id = id;
        this.nombre = nombre;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.btMAC = btMAC;
        this.correo = correo;
        this.Asistio = asistio;
    }

    @Override
    public String toString() {
        return getId() + " - " + getNombreCompleto();
    }

    public String getNombreCompleto() {
        return getApaterno() + " " + getAmaterno() + ", " + getNombre();
    }

    public String getAsistio() {
        return Asistio;
    }

    public void setAsistio(String asistio) {
        Asistio = asistio;
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
