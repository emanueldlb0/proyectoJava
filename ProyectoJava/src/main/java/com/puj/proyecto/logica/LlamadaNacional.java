package com.puj.proyecto.logica;

import java.time.LocalDate;

public class LlamadaNacional extends Llamada {

        public LlamadaNacional(long duracion, LocalDate fecha, long telefonoDestinatario) {
            super(duracion, fecha, telefonoDestinatario);
        }

        @Override
        public long calcularValor() {
            long costoPorMinuto = 200;
            long costoTotal = this.getDuracion() * costoPorMinuto;
            this.setValor(costoTotal);
            return costoTotal;
        }
    }
