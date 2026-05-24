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

    public cliente buscarCliente(long identificacion) {
        for (cliente c : clientes) {
            if (c.getIdentificacion() == identificacion) {
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

    @Override
    public boolean registrarLlamadaA_Cuenta(long numeroTelefono, Llamada nuevaLlamada) {
        for (Cuenta cuenta : this.cuentas) {
            if (cuenta.getNumero() == numeroTelefono) {
                long costoLlamada = nuevaLlamada.calcularValor();
                if (cuenta instanceof Prepago) {
                    Prepago prepago = (Prepago) cuenta;
                    int mes = nuevaLlamada.getFecha().getMonthValue();
                    int anio = nuevaLlamada.getFecha().getYear();
                    if (!prepago.verificarYConsumirSaldoParaLlamada(costoLlamada, mes, anio)) {
                        System.out.println(">>> [BLOQUEADO] Saldo insuficiente para realizar la llamada en este mes.");
                        return false;
                    }
                }
                cuenta.getLlamadas().add(nuevaLlamada);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean registrarRecargaA_Cuenta(long numeroTelefono, recarga nuevaRecarga) {
        for (Cuenta cuenta : this.cuentas) {
            if (cuenta.getNumero() == numeroTelefono) {
                if (cuenta instanceof Prepago) {
                    Prepago cuentaPrepago = (Prepago) cuenta;
                    cuentaPrepago.getRecargas().add(nuevaRecarga);
                    return true;
                }
            }
        }
        return false;
    }
    public void mostrarReportePostpago() {
        System.out.println("\n==================================================");
        System.out.println("   REPORTE DE CONSUMO Y FACTURACIÓN POSTPAGO      ");
        System.out.println("==================================================");
        boolean hayPostpago = false;

        for (Cuenta cuenta : this.cuentas) {
            if (cuenta instanceof Postpago) {
                hayPostpago = true;
                Postpago post = (Postpago) cuenta;
                long totalLlamadas = 0;
                for (Llamada ll : post.getLlamadas()) {
                    totalLlamadas += ll.getValor();
                }
                long totalAPagar = post.getCargoFijo() + totalLlamadas;
                System.out.println("Cliente: " + post.getCliente().getNombre() + " (" + post.getCliente().getIdentificacion() + ")");
                System.out.println("Línea No: " + post.getNumero());
                System.out.println("  -> Cargo Fijo: $" + post.getCargoFijo());
                System.out.println("  -> Consumo Llamadas: $" + totalLlamadas);
                System.out.println("  -> TOTAL A PAGAR: $" + totalAPagar);
                System.out.println("--------------------------------------------------");
            }
        }
        if (!hayPostpago) {
            System.out.println("No se encuentran cuentas Postpago registradas.");
        }
    }
    public void mostrarReportePrepago(int mes, int anio) {
        System.out.println("\n==================================================");
        System.out.println("   REPORTE DE ESTADO DE CUENTAS PREPAGO (" + mes + "/" + anio + ") ");
        System.out.println("==================================================");
        boolean hayPrepago = false;

        for (Cuenta cuenta : this.cuentas) {
            if (cuenta instanceof Prepago) {
                hayPrepago = true;
                Prepago prep = (Prepago) cuenta;


                long totalRecargado = prep.calcularSaldoDisponibleEnMes(mes, anio);


                long totalGastado = 0;
                for (Llamada ll : prep.getLlamadas()) {
                    if (ll.getFecha().getMonthValue() == mes && ll.getFecha().getYear() == anio) {
                        totalGastado += ll.getValor();
                    }
                }

                long saldoActual = totalRecargado - totalGastado;

                System.out.println("Cliente: " + prep.getCliente().getNombre() + " (" + prep.getCliente().getIdentificacion() + ")");
                System.out.println("Línea No: " + prep.getNumero());
                System.out.println("  -> Total Recargado en el mes: $" + totalRecargado);
                System.out.println("  -> Total Consumido en llamadas: $" + totalGastado);
                System.out.println("  -> SALDO DISPONIBLE: $" + saldoActual);
                System.out.println("--------------------------------------------------");
            }
        }
        if (!hayPrepago) {
            System.out.println("No se encuentran cuentas Prepago registradas.");
        }
    }
}