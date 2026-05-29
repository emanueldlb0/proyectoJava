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
        cliente cliente= buscarCliente(identificacionCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("reporte de facturacion postpago" + mes + "/" + año);
        System.out.println("Cliente: " + cliente.getNombre() + " ID: " + cliente.getIdentificacion());

        boolean tieneCuenta = false;
        for (Cuenta cuenta : this.cuentas) {
            if (cuenta instanceof Postpago && cuenta.getCliente().getIdentificacion() == identificacionCliente) {
                tieneCuenta = true;
                Postpago post = (Postpago) cuenta;
                System.out.println("Cuenta Postpago ID: " + post.getId() + " Número: " + post.getNumero());
                System.out.println("  Cargo Fijo: $" + post.getCargoFijo());

                java.util.ArrayList<Llamada> llamadasMes = new java.util.ArrayList<>();
                for (Llamada llamada: post.getLlamadas()) {
                    if (llamada.getFecha().getMonthValue() == mes && llamada.getFecha().getYear() == año) {
                        llamadasMes.add(llamada);
                    }
                }
                llamadasMes.sort(Comparator.comparing(Llamada::getFecha));

                long totalDuracion = 0, totalValor = 0;
                for (Llamada llamada : llamadasMes) {
                    totalDuracion += llamada.getDuracion();
                    totalValor += llamada.getValor();
                    System.out.println(" Fecha: " + llamada.getFecha()
                            + " | Duración: " + llamada.getDuracion() + " min"
                            + " | Destino: " + llamada.getTelefonoDestinatario()
                            + " | Valor: $" + llamada.getValor());
                }
                System.out.println("  Total duración: " + totalDuracion + " min | Total llamadas: $" + totalValor);
                System.out.println("  TOTAL A PAGAR: $" + (post.getCargoFijo() + totalValor));
            }
        }
        if (!tieneCuenta) {
            System.out.println("  (Este cliente no tiene cuentas postpago)");
        }
    }
    public void mostrarReportePrepago(int mes, int año) {
        System.out.println("   reporte recargas prepago " + mes + "/" + año);

        ArrayList<cliente> clientesConPrepago = new ArrayList<>();
        for (cliente cliente : this.clientes) {
            for (Cuenta c : this.cuentas) {
                if (c instanceof Prepago && c.getCliente().getIdentificacion() == cliente.getIdentificacion()) {
                    if (!clientesConPrepago.contains(cliente)) clientesConPrepago.add(cliente);
                    break;
                }
            }
        }
        clientesConPrepago.sort(Comparator.comparingLong(cliente::getIdentificacion));

        if (clientesConPrepago.isEmpty()) {
            System.out.println("No hay clientes con cuentas prepago.");
            return;
        }

        long granTotalRecargas = 0, granTotalDuracion = 0;

        for (cliente cliente : clientesConPrepago) {
            long totalRecargasCliente = 0, totalDuracionCliente = 0;
            System.out.println("Cliente: " + cliente.getNombre()
                    + " " + cliente.getTipoId() + ": " + cliente.getIdentificacion()
                    + " Dir: " + cliente.getDireccion());

            for (Cuenta cuenta : this.cuentas) {
                if (cuenta instanceof Prepago && cuenta.getCliente().getIdentificacion() == cliente.getIdentificacion()) {
                    Prepago prep = (Prepago) cuenta;
                    System.out.println("  Cuenta Prepago ID: " + prep.getId() + " Número: " + prep.getNumero());

                    ArrayList<Llamada> llamadasMes = new ArrayList<>();
                    for (Llamada llamada: prep.getLlamadas()) {
                        if (llamada.getFecha().getMonthValue() == mes && llamada.getFecha().getYear() == año)
                            llamadasMes.add(llamada);
                    }
                    llamadasMes.sort(java.util.Comparator.comparing(Llamada::getFecha));
                    long totalDurCuenta = 0, totalValCuenta = 0;
                    for (Llamada llamada : llamadasMes) {
                        totalDurCuenta += llamada.getDuracion();
                        totalValCuenta += llamada.getValor();
                        System.out.println("Llamada " + llamada.getFecha()
                                + " | " + llamada.getDuracion() + " min"
                                + " | Destino: " + llamada.getTelefonoDestinatario()
                                + " | $" + llamada.getValor());
                    }
                    System.out.println("  Total llamadas: " + totalDurCuenta + " min | valor $" + totalValCuenta);
                    totalDuracionCliente += totalDurCuenta;

                    ArrayList<recarga> recargasMes = new ArrayList<>();
                    for (recarga r : prep.getRecargas()) {
                        if (r.getFecha().getMonthValue() == mes && r.getFecha().getYear() == año)
                            recargasMes.add(r);
                    }
                    recargasMes.sort(Comparator.comparing(recarga::getFecha));
                    long totalRecCuenta = 0;
                    for (recarga r : recargasMes) {
                        totalRecCuenta += (long) r.getValor();
                        System.out.println(" Recarga " + r.getFecha() + " | $" + (long) r.getValor());
                    }
                    System.out.println("  Total recargas: $" + totalRecCuenta);
                    totalRecargasCliente += totalRecCuenta;
                }
            }
            System.out.println("total cliente - Recargas: $" + totalRecargasCliente + " | Duración: " + totalDuracionCliente + " min");
            granTotalRecargas += totalRecargasCliente;
            granTotalDuracion += totalDuracionCliente;
        }

        System.out.println("total general - Recargas: $" + granTotalRecargas + " | Duración: " + granTotalDuracion + " min");
    }
    }