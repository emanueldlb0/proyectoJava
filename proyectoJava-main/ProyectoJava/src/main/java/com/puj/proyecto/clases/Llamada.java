package com.puj.proyecto.logica;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Llamada implements Serializable {
    private long duracion;
    private LocalDate fecha;
    private long telefonoDestinatario;
    private long valor;

    public Llamada(long duracion, LocalDate fecha, long telefonoDestinatario) {
        this.duracion = duracion;
        this.fecha = fecha;
        this.telefonoDestinatario = telefonoDestinatario;
        this.valor = 0;
    }

    public abstract long calcularValor();

    public long getDuracion() { return duracion; }
    public void setDuracion(long duracion) { this.duracion = duracion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public long getTelefonoDestinatario() { return telefonoDestinatario; }
    public void setTelefonoDestinatario(long telefonoDestinatario) { this.telefonoDestinatario = telefonoDestinatario; }
    public long getValor() { return valor; }
    public void setValor(long valor) { this.valor = valor; }

    @Override
    public String toString() {
        return "Fecha: " + fecha + " | Duracion: " + duracion + " min | Destino: " + telefonoDestinatario + " | Valor: $" + valor;
    }
}