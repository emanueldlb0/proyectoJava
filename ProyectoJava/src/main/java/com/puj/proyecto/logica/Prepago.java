package com.puj.proyecto.logica;

import java.util.ArrayList;

public class Prepago extends Cuenta
{
    private long numeroMinutos;
    private ArrayList<recarga> recargas;

    public Prepago(long numero, cliente cliente) {
        super(numero, cliente);
        this.numeroMinutos = 5;
        this.recargas = new ArrayList<>();
    }

    public long getNumeroMinutos() {
        return numeroMinutos;
    }

    public void setNumeroMinutos(long numeroMinutos) {
        this.numeroMinutos = numeroMinutos;
    }

    public ArrayList<recarga> getRecargas() {
        return recargas;
    }

    public void setRecargas(ArrayList<recarga> recargas) {
        this.recargas = recargas;
    }
    @Override
    public long obtenerPagoCuenta() {
        return 0;
    }
}
