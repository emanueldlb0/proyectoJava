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
    public long calcularSaldoDisponibleEnMes(int mes, int anio) {
        long totalRecargasMes = 0;
        for (recarga r : this.recargas) {
            if (r.getFecha().getMonthValue() == mes && r.getFecha().getYear() == anio) {
                totalRecargasMes += r.getValor();
            }
        }
        return totalRecargasMes;
    }
    public boolean verificarYConsumirSaldoParaLlamada(long costoLlamada, int mes, int anio) {
        long saldoDisponibleDinero = calcularSaldoDisponibleEnMes(mes, anio);
        if (saldoDisponibleDinero >= costoLlamada) {
            return true;
        }
        return false;
    }
}
