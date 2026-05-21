package com.puj.proyecto.logica;

import java.io.Serializable;
import java.util.ArrayList;

public class Empresa implements IEmpresa, Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private ArrayList<cliente> clientes;
    private ArrayList<Cuenta> cuentas;

    public Empresa(String nombre) {
        this.nombre = nombre;
        this.clientes = new ArrayList<>();
        this.cuentas = new ArrayList<>();
    }

    @Override
    public ArrayList<cliente> getClientes() {
        return this.clientes;
    }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public ArrayList<Cuenta> getCuentas() { return cuentas; }
    public void setCuentas(ArrayList<Cuenta> cuentas) { this.cuentas = cuentas; }
    public cliente buscarCliente(long identificacion) { // Cambiado a long
        for (cliente c : clientes) {
            if (c.getIdentificacion() == identificacion) { // Corregido a ==
                return c;
            }
        }
        return null;
    }
    public boolean crearCuentaCliente(long identificacionCliente, long numeroTelefono, String tipoCuenta) {
        cliente clienteEncontrado = null;
        for (cliente c : this.clientes) {
            if (c.getIdentificacion() == identificacionCliente) {
                clienteEncontrado = c;
                break;
            }
        }

        if (clienteEncontrado == null) {
            return false;
        }

        for (Cuenta cuenta : this.cuentas) {
            if (cuenta.getNumero() == numeroTelefono) {
                return false;
            }
        }

        Cuenta nuevaCuenta;
        if (tipoCuenta.equalsIgnoreCase("prepago")) {
            nuevaCuenta = new Prepago(numeroTelefono, clienteEncontrado);
        } else if (tipoCuenta.equalsIgnoreCase("postpago")) {
            nuevaCuenta = new Postpago(numeroTelefono, clienteEncontrado);
        } else {
            return false;
        }

        this.cuentas.add(nuevaCuenta);
        return true;
    }

    public boolean registrarLlamadaA_Cuenta(long numeroTelefono, Llamada nuevaLlamada) {
        for (Cuenta cuenta : this.cuentas) {
            if (cuenta.getNumero() == numeroTelefono) {
                nuevaLlamada.calcularValor();
                cuenta.getLlamadas().add(nuevaLlamada);
                return true;
            }
        }
        return false;
    }
}