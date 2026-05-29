package com.puj.proyecto.app;

import com.puj.proyecto.logica.*;
import com.puj.proyecto.persistencia.ManejoArchivos;

import java.time.LocalDate;
import java.util.Scanner;

public class App 
{       private static IEmpresa javeMovil = new Empresa("Javemovil");

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int opcion = 0;

            do {
                System.out.println(" menu Javemovil");
                System.out.println("1. Cargar clientes desde archivo");
                System.out.println("2. Agregar cuenta a un cliente");
                System.out.println("3. Registrar llamada");
                System.out.println("4. Registrar recarga");
                System.out.println("5. Reporte de facturación (Postpago)");
                System.out.println("6. Reporte de recargas (Prepago)");
                System.out.println("7. Salvar/Cargar sistema");
                System.out.println("8. Salir");
                System.out.print("Seleccione una opción: ");

                opcion = sc.nextInt();

                switch (opcion) {
                    case 1:
                        System.out.println("1. Seleccionar archivo de clientes");
                        System.out.println("2. Regresar al menú");
                        System.out.print("Opción: ");
                        int opcionCarga = sc.nextInt();
                        if (opcionCarga == 1) {
                            System.out.print("Ingrese la ruta del archivo: ");
                            String rutaArchivo = sc.next().trim();
                            ManejoArchivos.cargarClientesTxt(rutaArchivo, (Empresa) javeMovil);
                        }
                        break;
                    case 2:
                        System.out.print("Ingrese la identificación del cliente: ");
                        long idCliente = sc.nextLong();
                        System.out.print("Ingrese el numero de teléfono para la nueva cuenta: ");
                        long numeroTel = sc.nextLong();
                        System.out.print("Ingrese el tipo de cuenta (prepago / postpago): ");
                        String tipo = sc.next().trim();

                        boolean exito = javeMovil.crearCuentaCliente(idCliente, numeroTel, tipo);
                        if (exito) {
                            System.out.println("cuenta creada exitosamente.");
                        } else {
                            System.out.println("Error: Cliente no encontrado, número duplicado o tipo inválido.");
                        }
                        break;
                    case 3:
                        System.out.println("registrar llamada");
                        System.out.print("Ingrese el número de teléfono origen de la cuenta: ");
                        long numOrigen = sc.nextLong();

                        System.out.print("Ingrese la duración de la llamada (en minutos): ");
                        long duracion = sc.nextLong();

                        LocalDate fechaActual = java.time.LocalDate.now();

                        System.out.print("llamada Nacional(1) o Internacional(2)? ");
                        int tipoLlamada = sc.nextInt();

                        System.out.print("Ingrese el número telefonico del destinatario: ");
                        long telDestinatario = sc.nextLong();

                        Llamada nuevaLlamada = null;

                        if (tipoLlamada == 1) {
                            nuevaLlamada = new LlamadaNacional(duracion, fechaActual, telDestinatario);
                        } else if (tipoLlamada == 2) {

                            System.out.print("Ingrese el pais de destino: ");
                            sc.nextLine();
                            String paisDestino = sc.nextLine().trim();
                            nuevaLlamada = new LlamadaInternacional(duracion, fechaActual, telDestinatario, paisDestino);
                        } else {
                            System.out.println("Error: Tipo de llamada no valido.");
                            break;
                        }

                        boolean llamadaRegistrada = javeMovil.registrarLlamadaA_Cuenta(numOrigen, nuevaLlamada);
                        if (llamadaRegistrada) {
                            System.out.println("Llamada registrada" + nuevaLlamada.toString());
                        } else {
                            System.out.println("Error: No se encontró la cuenta.");
                        }
                        break;
                    case 4:
                        System.out.println("Registrar recarga");
                        System.out.print("Ingrese el número de teléfono de la cuenta prepago: ");
                        long numTelRecarga = sc.nextLong();

                        System.out.print("Ingrese la fecha de la recarga: ");
                        String fechaStr = sc.next().trim();
                        java.time.LocalDate fechaRecarga = com.puj.proyecto.utilidades.utils.stringToLocalDate(fechaStr);

                        System.out.print("Ingrese el valor de la recarga:");
                        double valorRecarga = sc.nextDouble();

                        recarga nuevaRecarga = new recarga(fechaRecarga, valorRecarga);
                        boolean recargaExitosa = javeMovil.registrarRecargaA_Cuenta(numTelRecarga, nuevaRecarga);

                        if (recargaExitosa) {
                            System.out.println("Recarga registrada exitosamente");
                        } else {
                            System.out.println("No se encontró la cuenta o no es de tipo Prepago.");
                        }
                        break;
                    case 5:
                        System.out.print("Ingrese el año: ");
                        int añoPostpago = sc.nextInt();
                        System.out.print("Ingrese el mes (1-12): ");
                        int mesPostpago = sc.nextInt();
                        System.out.println("Clientes registrados:");
                        for (cliente clienteListado : javeMovil.getClientes()) {
                            System.out.println("  " + clienteListado.getIdentificacion() + " - " + clienteListado.getNombre());
                        }
                        System.out.print("Ingrese la identificación del cliente: ");
                        long identificacionCliente = sc.nextLong();
                        javeMovil.mostrarReportePostpago(identificacionCliente, mesPostpago, añoPostpago);
                        break;
                    case 6:
                        System.out.println("Consulta de reportes prepago");
                        System.out.print("Ingrese el número del mes a consultar (1-12): ");
                        int mesRep = sc.nextInt();
                        System.out.print("Ingrese el año a consultar: ");
                        int añoRep = sc.nextInt();

                        javeMovil.mostrarReportePrepago(mesRep, añoRep);
                        break;
                    case 7:
                        System.out.println("serializacion");
                        System.out.println("1. Salvar todo el estado del sistema");
                        System.out.println("2. Cargar estado del sistema desde archivo binario");
                        System.out.print("Seleccione una opción: ");
                        int opcionPersistencia = sc.nextInt();

                        if (opcionPersistencia == 1) {
                            com.puj.proyecto.persistencia.ManejoArchivos.salvarSistema("javemovil_datos.dat", (Empresa) javeMovil);
                        } else if (opcionPersistencia == 2) {
                            Empresa recuperada = com.puj.proyecto.persistencia.ManejoArchivos.cargarSistema("javemovil_datos.dat");
                            if (recuperada != null) {
                                javeMovil = recuperada;
                                System.out.println("Sistema cargado exitosamente");
                            } else {
                                System.out.println("No se encontraron datos guardados.");
                            }
                        } else {
                            System.out.println("Opción inválida.");
                        }
                        break;
                    case 8:
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } while (opcion != 8);
        }
    }

