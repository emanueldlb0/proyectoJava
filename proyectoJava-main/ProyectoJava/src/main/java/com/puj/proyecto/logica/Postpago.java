package com.puj.proyecto.logica;

public class Postpago extends Cuenta
{
    private long cargoFijo;

    public Postpago(long numero, cliente cliente) {
        super(numero, cliente);
        this.cargoFijo = 20000;
    }

    public long getCargoFijo() {
        return cargoFijo;
    }

    public void setCargoFijo(long cargoFijo) {
        this.cargoFijo = cargoFijo;
    }

    @Override
    public long obtenerPagoCuenta() {
        return this.cargoFijo;
    }


}
