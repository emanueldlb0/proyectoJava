package com.puj.proyecto.persistencia;

import com.puj.proyecto.logica.Empresa;

import com.puj.proyecto.logica.cliente;
import com.puj.proyecto.logica.Empresa;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ManejoArchivos {

    public static void cargarClientesTxt(String rutaArchivo, Empresa empresa) {
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

    public static void salvarSistema(String rutaArchivo, Empresa empresa) {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(rutaArchivo))) {
            oos.writeObject(empresa);
            System.out.println("Sistema guardado");
        } catch (java.io.IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
    }

    public static Empresa cargarSistema(String rutaArchivo) {
        java.io.File archivo = new java.io.File(rutaArchivo);
        if (!archivo.exists()) {
            return null;
        }
        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(rutaArchivo))) {
            return (Empresa) ois.readObject();
        } catch (java.io.IOException | ClassNotFoundException e) {
            System.out.println("Error al recuperar el sistema: " + e.getMessage());
            return null;
        }
    }
}