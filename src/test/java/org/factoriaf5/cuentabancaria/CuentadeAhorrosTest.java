package org.factoriaf5.cuentabancaria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CuentadeAhorrosTest {

    private CuentadeAhorros cuenta;

    @BeforeEach
    public void setUp() {
        // Inicializamos la cuenta con saldo de 15000 y una tasa de 0.05 (5%)
        cuenta = new CuentadeAhorros(15000, 0.05f);
    }

    @Test
    public void testConsignarEnCuentaActiva() {
        // Consignamos una cantidad en una cuenta activa
        cuenta.consignar(500);
        // El saldo debería ser 15500 después de la consignación
        assertEquals(15500, cuenta.saldo, "El saldo debe ser 15500 después de la consignación");
    }

    @Test
    public void testConsignarEnCuentaInactiva() {
        // Desactivamos la cuenta poniéndola con saldo menor a 10000
        cuenta = new CuentadeAhorros(5000, 0.05f);
        // Intentamos consignar en una cuenta inactiva
        cuenta.consignar(1000);
        // La consignación no debe ser permitida, por lo tanto el saldo sigue siendo 5000
        assertEquals(5000, cuenta.saldo, "El saldo no debe cambiar si la cuenta está inactiva");
    }

    @Test
    public void testRetirarDeCuentaActiva() {
        // Realizamos un retiro en una cuenta activa
        cuenta.retirar(500);
        // El saldo debe ser 14500 después del retiro
        assertEquals(14500, cuenta.saldo, "El saldo debe ser 14500 después del retiro");
    }

    @Test
    public void testRetirarDeCuentaInactiva() {
        // Desactivamos la cuenta poniéndola con saldo menor a 10000
        cuenta = new CuentadeAhorros(5000, 0.05f);
        // Intentamos retirar en una cuenta inactiva
        boolean resultado = cuenta.retirar(1000);
        // El retiro no debe ser permitido en una cuenta inactiva
        assertFalse(resultado, "El retiro no debe ser permitido si la cuenta está inactiva");
        assertEquals(5000, cuenta.saldo, "El saldo no debe cambiar si el retiro no se realiza");
    }

    @Test
    public void testExtractoMensualConRetiros() {
        // Realizamos 5 retiros (debe generarse comisión por uno de ellos)
        cuenta.retirar(1000); // Saldo: 14000
        cuenta.retirar(1000); // Saldo: 13000
        cuenta.retirar(1000); // Saldo: 12000
        cuenta.retirar(1000); // Saldo: 11000
        cuenta.retirar(1000); // Saldo: 10000

        // El saldo después de los retiros debería ser 10000, con una comisión de 1000
        cuenta.extractoMensual();
        
        // Verificamos que la comisión mensual esté calculada correctamente
        assertEquals(10000 + 1000 * 0.05f / 12, cuenta.saldo, 0.01, "El saldo debe reflejar los intereses y la comisión mensual");
        assertEquals(1000, cuenta.comisionMensual, "La comisión mensual debe ser de 1000 después de más de 4 retiros");
    }

    @Test
    public void testCuentaInactivaPorBajoSaldo() {
        // Desactivamos la cuenta poniéndola con saldo menor a 10000
        cuenta = new CuentadeAhorros(5000, 0.05f);
        // Verificamos si la cuenta está inactiva
        assertFalse(cuenta.isActiva(), "La cuenta debe estar inactiva si el saldo es menor a 10000");
    }

    @Test
    public void testExtractoMensualConCambioDeEstado() {
        // Realizamos 5 retiros (debe generarse comisión por uno de ellos)
        cuenta.retirar(1000); // Saldo: 14000
        cuenta.retirar(1000); // Saldo: 13000
        cuenta.retirar(1000); // Saldo: 12000
        cuenta.retirar(1000); // Saldo: 11000
        cuenta.retirar(1000); // Saldo: 10000

        // Realizamos el extracto mensual
        cuenta.extractoMensual();
        
        // Verificamos si la cuenta se mantiene activa (porque el saldo es >= 10000)
        assertTrue(cuenta.isActiva(), "La cuenta debe permanecer activa si el saldo es mayor o igual a 10000");
    }

    @Test
    public void testImprimirCuentaActiva() {
        // Imprimimos el estado de la cuenta activa
        String expected = "Saldo: 15000.00, Comisiones: 0.00, Transacciones: 0, Sobregiro: 0.00, Activa: true";
        assertEquals(expected, cuenta.imprimir(), "El formato de impresión de la cuenta activa debe ser correcto");
    }

    @Test
    public void testImprimirCuentaInactiva() {
        // Desactivamos la cuenta poniéndola con saldo menor a 10000
        cuenta = new CuentadeAhorros(5000, 0.05f);
        // Imprimimos el estado de la cuenta inactiva
        String expected = "Saldo: 5000.00, Comisiones: 0.00, Transacciones: 0, Sobregiro: 0.00, Activa: false";
        assertEquals(expected, cuenta.imprimir(), "El formato de impresión de la cuenta inactiva debe ser correcto");
    }
}
