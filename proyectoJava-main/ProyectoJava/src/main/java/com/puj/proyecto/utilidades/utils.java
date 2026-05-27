package com.puj.proyecto.utilidades;

import java.time.LocalDate;

public class utils {
    public static long CONSECUTIVO = 0;

    public static LocalDate stringToLocalDate(String fecha) {
        String[] partes = fecha.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int año = Integer.parseInt(partes[2]);

        return LocalDate.of(año, mes, dia);
    }

    public static String localDateToString(LocalDate fecha) {
        return fecha.getDayOfMonth() + "/" + fecha.getMonthValue() + "/" + fecha.getYear();
    }
}

