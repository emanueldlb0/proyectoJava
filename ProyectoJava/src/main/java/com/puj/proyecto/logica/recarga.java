package com.puj.proyecto.logica;

import java.io.Serializable;
import java.time.LocalDate;

public class recarga implements Serializable {
    private LocalDate fecha;
    private double valor;

    public LocalDate getFecha() {
        return fecha;
    }

    public double getValor() {
        return valor;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public recarga(LocalDate fecha, double valor) {
        this.fecha = fecha;
        this.valor = valor;
    }
}
