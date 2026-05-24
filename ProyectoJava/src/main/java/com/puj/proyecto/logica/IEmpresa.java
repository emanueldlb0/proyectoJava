package com.puj.proyecto.logica;

import java.util.ArrayList;

public interface IEmpresa {
    ArrayList<cliente> getClientes();
    boolean crearCuentaCliente(long identificacionCliente, long numeroTelefono, String tipoCuenta);
    boolean registrarLlamadaA_Cuenta(long numeroTelefono, Llamada nuevaLlamada);
    boolean registrarRecargaA_Cuenta(long numeroTelefono, recarga nuevaRecarga);

    // === NUEVAS LÍNEAS PARA QUITAR EL ERROR DE APP.JAVA ===
    void mostrarReportePostpago();
    void mostrarReportePrepago(int mes, int anio);
}