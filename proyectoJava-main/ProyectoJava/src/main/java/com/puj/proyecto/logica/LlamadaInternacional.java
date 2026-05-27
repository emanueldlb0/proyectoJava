package com.puj.proyecto.logica;

import java.time.LocalDate;

public class LlamadaInternacional extends Llamada {
    private String paisDestino;
    public LlamadaInternacional(long duracion, LocalDate fecha, long telefonoDestinatario, String paisDestino) {
        super(duracion, fecha, telefonoDestinatario);
        this.paisDestino = paisDestino;
    }

    @Override
    public long calcularValor() {
        long costoBaseMinutoInternacional = 500;
        long costoBase = this.getDuracion() * costoBaseMinutoInternacional;
        long recargo = (long) (costoBase * 0.20);
        long costoTotal = costoBase + recargo;
        this.setValor(costoTotal);
        return costoTotal;
    }

    public String getPaisDestino() { return paisDestino; }
    public void setPaisDestino(String paisDestino) { this.paisDestino = paisDestino; }
}
