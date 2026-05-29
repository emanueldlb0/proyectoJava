package com.puj.proyecto.logica;

import java.io.Serializable;

public class cliente implements Serializable {
        private long identificacion;
        private String nombre;
        private String tipoId;
        private String direccion;

    public long getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoId() {
        return tipoId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setIdentificacion(long identificacion) {
        this.identificacion = identificacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public cliente(long identificacion, String nombre, String tipoId, String direccion) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.tipoId = tipoId;
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "cliente{" +
                "identificacion=" + identificacion +
                ", nombre='" + nombre + '\'' +
                ", tipoId='" + tipoId + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }

}
