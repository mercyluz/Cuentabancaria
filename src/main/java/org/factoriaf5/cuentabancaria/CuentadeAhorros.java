package org.factoriaf5.cuentabancaria;

public class CuentadeAhorros extends Cuenta {
    private boolean activa;

    public CuentadeAhorros(float saldo, float tasaAnual) {
        super(saldo, tasaAnual);
        this.setActiva(saldo >= 10000);
    }

    public boolean isActiva() {
        return activa;
        
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
        
    }

    @Override
    public void consignar(float cantidad) {
        if (isActiva()) {
            super.consignar(cantidad);
        }
    }

    @Override
    public boolean retirar(float cantidad) {
        if (isActiva()) {
            return super.retirar(cantidad);
        }
        return false;
    }

    @Override
    public void extractoMensual() {
        if (numRetiros > 4) {
            comisionMensual = (numRetiros - 4) * 1000;
        }
        super.extractoMensual();
        if (saldo < 10000) {
            setActiva(false);
        }
    }

    @Override
    public String imprimir() {
        return super.imprimir() + String.format(", Activa: %b", isActiva());
    }
}
