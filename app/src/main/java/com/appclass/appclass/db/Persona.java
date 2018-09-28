package com.appclass.appclass.db;

public class Persona {
    private String nombre;
    private String apaterno;
    private String amaterno;
    private String btMAC;
    private String correo;

    public Persona() {
    }

    public Persona(String nombre, String apaterno, String amaterno, String btMAC, String correo) {
        this.nombre = nombre;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.btMAC = btMAC;
        this.correo = correo;
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
