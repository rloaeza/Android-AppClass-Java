package com.appclass.appclass;
//http://www.hermosaprogramacion.com/2014/10/android-listas-adaptadores/
public class ItemClase {
    private String nombreClase;
    private String descripcion;
    private String codigo;
    private boolean activa;
    private int cantidadAlumnos;

    public ItemClase(String nombreClase, String descripcion, String codigo, boolean activa, int cantidadAlumnos) {
        this.nombreClase = nombreClase;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.activa = activa;
        this.cantidadAlumnos = cantidadAlumnos;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public int getCantidadAlumnos() {
        return cantidadAlumnos;
    }

    public void setCantidadAlumnos(int cantidadAlumnos) {
        this.cantidadAlumnos = cantidadAlumnos;
    }
}
