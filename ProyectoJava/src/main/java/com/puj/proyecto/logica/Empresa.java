package com.puj.proyecto.logica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

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
                        System.out.println("Saldo insuficiente para realizar la llamada en este mes.");
                        return false;
                    }
                }

                    if (nuevaLlamada instanceof LlamadaInternacional) {
                    LlamadaInternacional llamada = (LlamadaInternacional) nuevaLlamada;
                    if (llamada.getIndicativo() != null) {
                        long telConIndicativo = Long.parseLong(
                                llamada.getIndicativo().getCodigo() + "" + llamada.getTelefonoDestinatario()
                        );
                        llamada.setTelefonoDestinatario(telConIndicativo);
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
    public void mostrarReportePostpago(long identificacionCliente, int mes, int año) {
        cliente clienteEncontrado = buscarCliente(identificacionCliente);
        if (clienteEncontrado == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("reporte postpago- " + mes + "/" + año);
        System.out.println("Cliente: " + clienteEncontrado.getNombre() + " | " + clienteEncontrado.getTipoId() + ": " + clienteEncontrado.getIdentificacion());
        System.out.println("Direccion: " + clienteEncontrado.getDireccion());

        boolean tieneCuentaPostpago = false;
        for (Cuenta cuenta : this.cuentas) {
            if (cuenta instanceof Postpago && cuenta.getCliente().getIdentificacion() == identificacionCliente) {
                tieneCuentaPostpago = true;
                Postpago cuentaPostpago = (Postpago) cuenta;
                System.out.println("Cuenta ID: " + cuentaPostpago.getId() + " Numero: " + cuentaPostpago.getNumero());
                System.out.println("Cargo Fijo:" + cuentaPostpago.getCargoFijo());

                ArrayList<Llamada> llamadasDelMes = new ArrayList<>();
                for (Llamada llamada : cuentaPostpago.getLlamadas()) {
                    if (llamada.getFecha().getMonthValue() == mes && llamada.getFecha().getYear() == año) {
                        llamadasDelMes.add(llamada);
                    }
                }
                llamadasDelMes.sort(Comparator.comparing(Llamada::getFecha));

                long totalDuracion = 0;
                long totalValorLlamadas = 0;
                for (Llamada llamada : llamadasDelMes) {
                    totalDuracion += llamada.getDuracion();
                    totalValorLlamadas += llamada.getValor();
                    System.out.println(llamada.toString());
                }
                System.out.println("Total duracion: " + totalDuracion + " min | Total llamadas: $" + totalValorLlamadas);
                System.out.println("Total a pagar: $" + (cuentaPostpago.getCargoFijo() + totalValorLlamadas));
            }
        }
        if (!tieneCuentaPostpago) {
            System.out.println("Este cliente no tiene cuentas postpago.");
        }
    }
    public void mostrarReportePrepago(int mes, int año) {
        boolean hayPrepago = false;

        for (Cuenta cuenta : this.cuentas) {
            if (cuenta instanceof Prepago) {
                hayPrepago = true;
                Prepago prep = (Prepago) cuenta;


                long totalRecargado = prep.calcularSaldoDisponibleEnMes(mes, año);


                long totalGastado = 0;
                for (Llamada ll : prep.getLlamadas()) {
                    if (ll.getFecha().getMonthValue() == mes && ll.getFecha().getYear() == año) {
                        totalGastado += ll.getValor();
                    }
                }

                long saldoActual = totalRecargado - totalGastado;

                System.out.println("Cliente: " + prep.getCliente().getNombre() + " (" + prep.getCliente().getIdentificacion() + ")");
                System.out.println("Línea No: " + prep.getNumero());
                System.out.println("total Recargado en el mes: $" + totalRecargado);
                System.out.println("total Consumido en llamadas: $" + totalGastado);
                System.out.println(" saldo disponible: $" + saldoActual);
            }
        }
        if (!hayPrepago) {
            System.out.println("No se encuentran cuentas Prepago registradas.");
        }
    }
}