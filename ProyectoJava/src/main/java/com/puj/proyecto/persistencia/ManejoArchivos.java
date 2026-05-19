package com.puj.proyecto.persistencia;

import com.puj.proyecto.logica.Empresa;

import com.puj.proyecto.logica.cliente;
import com.puj.proyecto.logica.Empresa;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ManejoArchivos {

    public static void cargarClientesTXT(String rutaArchivo, Empresa empresa) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                  String[] datos = linea.split("\\*");
                if (datos.length >= 5) {
                    String id = datos[1].trim();
                    String nombre = datos[2].trim();
                    String tipoId = datos[3].trim();
                    String direccion = datos[4].trim();
                    if (empresa.buscarCliente(Long.parseLong(id)) == null) {
                        cliente nuevoCliente = new cliente(Long.parseLong(id), nombre, tipoId, direccion);
                        empresa.getClientes().add(nuevoCliente);
                    }
                }
            }
            System.out.println(">>> ¡Clientes cargados exitosamente desde el TXT! Total: " + empresa.getClientes().size());

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de clientes: " + e.getMessage());
        }
    }
}