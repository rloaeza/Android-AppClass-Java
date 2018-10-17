package com.appclass.appclass.db;

public class Usuario {
    private String correo;
    private String nombre;
    private String apellidos;
    private String idControl;
    private String macBT;
    private boolean asistio;

    public Usuario(String correo, String nombre, String apellidos, String idControl, String macBT, boolean asistio) {
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idControl = idControl;
        this.macBT = macBT;
        this.asistio = asistio;
    }

    public Usuario() {
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getIdControl() {
        return idControl;
    }

    public void setIdControl(String idControl) {
        this.idControl = idControl;
    }

    public String getMacBT() {
        return macBT;
    }

    public void setMacBT(String macBT) {
        this.macBT = macBT;
    }

    public boolean isAsistio() {
        return asistio;
    }

    public void setAsistio(boolean asistio) {
        this.asistio = asistio;
    }



    public String getNombreCompleto() {
        return getIdControl() + ": " + getApellidos() + ", " + getNombre();
    }

    public String toString() {
        return getNombreCompleto();
    }
}
