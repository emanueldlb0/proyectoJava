package com.puj.proyecto.app;

import com.puj.proyecto.logica.Empresa;
import com.puj.proyecto.logica.IEmpresa;

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
                        break;
                    case 2:
                        System.out.print("Ingrese la identificación del cliente: ");
                        long idCliente = sc.nextLong();
                        System.out.print("Ingrese el número de teléfono para la nueva cuenta: ");
                        long numeroTel = sc.nextLong();
                        System.out.print("Ingrese el tipo de cuenta (prepago / postpago): ");
                        String tipo = sc.next().trim();

                        boolean exito = javeMovil.crearCuentaCliente(idCliente, numeroTel, tipo);
                        if (exito) {
                            System.out.println("Cuenta creada exitosamente.");
                        } else {
                            System.out.println("Error: Cliente no encontrado, número duplicado o tipo inválido.");
                        }
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } while (opcion != 8);
        }
    }

