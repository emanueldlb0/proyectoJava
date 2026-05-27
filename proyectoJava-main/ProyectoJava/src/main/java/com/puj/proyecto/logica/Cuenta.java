package com.puj.proyecto.logica;

import com.puj.proyecto.utilidades.utils;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    protected long id;
    protected long numero;
    protected cliente cliente;
    protected ArrayList<Llamada> llamadas;

    public Cuenta(long numero, cliente cliente) {
        this.id = utils.CONSECUTIVO++;
        this.numero = numero;
        this.cliente = cliente;
        this.llamadas = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public cliente getCliente() {
        return cliente;
    }

    public void setCliente(cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<Llamada> getLlamadas() {
        return llamadas;
    }

    public void setLlamadas(ArrayList<Llamada> llamadas) {
        this.llamadas = llamadas;
    }
    public abstract long obtenerPagoCuenta();
}
