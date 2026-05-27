package com.puj.proyecto.logica;

import java.util.ArrayList;

public interface IEmpresa {
    ArrayList<cliente> getClientes();
    boolean crearCuentaCliente(long identificacionCliente, long numeroTelefono, String tipoCuenta);
    boolean registrarLlamadaA_Cuenta(long numeroTelefono, Llamada nuevaLlamada);
    boolean registrarRecargaA_Cuenta(long numeroTelefono, recarga nuevaRecarga);

    void mostrarReportePostpago(long identificacionCliente, int mes, int año);
    void mostrarReportePrepago(int mes, int anio);
}