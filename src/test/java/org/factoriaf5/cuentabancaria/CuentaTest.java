package org.factoriaf5.cuentabancaria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CuentaTest {

    private Cuenta cuenta;

    @BeforeEach
    public void setUp() {
        cuenta = new Cuenta(10000, 0.05f); // saldo inicial 10000, tasa anual 5%
    }

    @Test
    public void testConsignar() {
        cuenta.consignar(500);
        assertEquals(10500, cuenta.saldo);
    }

    @Test
    public void testRetirar() {
        assertTrue(cuenta.retirar(3000));
        assertEquals(7000, cuenta.saldo);
    }

    @Test
    public void testRetiroInsuficiente() {
        assertFalse(cuenta.retirar(12000)); // No tiene suficiente saldo
        assertEquals(10000, cuenta.saldo);
    }

    @Test
    public void testCalcularInteresMensual() {
        cuenta.calcularInteresMensual();
        assertEquals(10000 * 0.05 / 12 + 10000, cuenta.saldo, 0.01);
    }

    @Test
    public void testExtractoMensual() {
        cuenta.extractoMensual();
        assertEquals(10000 * 0.05 / 12 + 10000 - cuenta.comisionMensual, cuenta.saldo, 0.01);
    }
}
