package org.factoriaf5.cuentabancaria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CuentaCorrienteTest {

    private CuentaCorriente cuenta;

    @BeforeEach
    public void setUp() {
        // Inicializamos la cuenta con saldo de 1000 y una tasa de 0.05 (5%)
        cuenta = new CuentaCorriente(1000, 0.05f);
    }

    @Test
    public void testConsignarSinSobregiro() {
        // Realizamos una consignación de 500
        cuenta.consignar(500);
        // El saldo debe ser 1500 (1000 + 500)
        assertEquals(1500, cuenta.saldo, "El saldo debe ser 1500 después de la consignación");
    }

    @Test
    public void testRetirarConSobregiro() {
        // Intentamos retirar 1200, que es mayor que el saldo
        cuenta.retirar(1200);
        // El saldo debe ser 0 y el sobregiro debe ser 200
        assertEquals(0, cuenta.saldo, "El saldo debe ser 0 después del retiro");
        assertEquals(200, cuenta.sobregiro, "El sobregiro debe ser 200 después del retiro");
    }

    @Test
    public void testConsignarParaCubrirSobregiro() {
        // Intentamos retirar más de lo que hay en la cuenta, generando un sobregiro
        cuenta.retirar(1200);  // Sobregiro de 200
        // Ahora consignamos 300 para cubrir el sobregiro
        cuenta.consignar(300);
        // El saldo debe ser 100 (300 - 200), y el sobregiro debe ser 0
        assertEquals(100, cuenta.saldo, "El saldo debe ser 100 después de cubrir el sobregiro");
        assertEquals(0, cuenta.sobregiro, "El sobregiro debe ser 0 después de cubrir el sobregiro");
    }

    @Test
    public void testConsignarMenosQueSobregiro() {
        // Intentamos retirar más de lo que hay en la cuenta, generando un sobregiro
        cuenta.retirar(1200);  // Sobregiro de 200
        // Ahora consignamos solo 100, lo cual cubre parcialmente el sobregiro
        cuenta.consignar(100);
        // El saldo debe ser 0 (ya que el sobregiro no se ha cubierto completamente)
        // Y el sobregiro debe ser reducido a 100
        assertEquals(0, cuenta.saldo, "El saldo debe ser 0 después de la consignación parcial");
        assertEquals(100, cuenta.sobregiro, "El sobregiro debe ser 100 después de la consignación parcial");
    }

    @Test
    public void testExtractoMensualSinTransacciones() {
        // No hacemos ninguna transacción
        cuenta.extractoMensual();
        // El saldo debería haber crecido por los intereses del mes, y la comisión debería ser 0
        float saldoEsperado = 1000 + (1000 * 0.05f / 12);
        assertEquals(saldoEsperado, cuenta.saldo, 0.01, "El saldo debería crecer por los intereses del mes");
    }

    @Test
    public void testExtractoMensualConTransacciones() {
        // Realizamos algunas operaciones antes del extracto
        cuenta.consignar(500);  // Saldo debe ser 1500
        cuenta.retirar(200);    // Saldo debe ser 1300
        // Ahora generamos el extracto mensual
        cuenta.extractoMensual();
        // Verificamos el saldo final (intereses + comisiones si es necesario)
        // El saldo final es el saldo + intereses (1300 * 0.05 / 12)
        double saldoEsperado = 1300 + (1300 * 0.05 / 12);
        assertEquals(saldoEsperado, cuenta.saldo, 0.01, "El saldo debe incluir los intereses mensuales calculados");
    }

    @Test
    public void testExtractoMensualConSobregiro() {
        // Realizamos algunas operaciones antes del extracto
        cuenta.retirar(1200);  // Sobregiro de 200
        cuenta.consignar(100);  // Saldo debe ser 0, sobregiro debe ser 100
        cuenta.extractoMensual();
        // El saldo final debe incluir intereses, pero aún debe estar en 0 ya que el sobregiro no se cubrió completamente
        float saldoEsperado = 0 + (0 * 0.05f / 12);  // No hay saldo para calcular los intereses
        assertEquals(saldoEsperado, cuenta.saldo, 0.01, "El saldo no debe tener intereses si es 0");
    }

    @Test
    public void testSobregiroNoCubreConConsignacion() {
        // Realizamos una retirada que genere un sobregiro
        cuenta.retirar(1500);  // Sobregiro de 500
        // Ahora intentamos consignar solo 200
        cuenta.consignar(200);
        // El saldo debe seguir en 0 y el sobregiro debe ser 300
        assertEquals(0, cuenta.saldo, "El saldo debe ser 0 después de la consignación parcial");
        assertEquals(300, cuenta.sobregiro, "El sobregiro debe ser 300 después de consignar solo 200");
    }

    @Test
    public void testImprimirCuentaCorriente() {
        // Realizamos operaciones de consignación y retiro
        cuenta.consignar(500);
        cuenta.retirar(300);
        // Ahora verificamos la salida del método imprimir
        String expected = "Saldo: 1200.00, Comisiones: 0.00, Transacciones: 2, Sobregiro: 0.00";
        assertEquals(expected, cuenta.imprimir(), "El formato del extracto de la cuenta debe ser el esperado");
    }

    @Test
    public void testImprimirCuentaConSobregiro() {
        // Inicializamos la cuenta corriente con un saldo de 1000
        cuenta = new CuentaCorriente(1000, 0.05f);
        
        // Realizamos un retiro de 1200 (esto debería generar un sobregiro de 200)
        cuenta.retirar(1200); // Sobregiro de 200
        // Ahora consignamos 100 (esto cubriría parcialmente el sobregiro)
        cuenta.consignar(100); // Saldo 0, sobregiro 100
        
        // Ahora verificamos la salida del método imprimir
        String expected = "Saldo: 0.00, Comisiones: 0.00, Transacciones: 2, Sobregiro: 100.00";
        assertEquals(expected, cuenta.imprimir(), "El formato del extracto de la cuenta con sobregiro debe ser el esperado");
    }
    

    @Test
    public void testRetiroSinSuficienteSaldoParaSobregiro() {
        // Inicializamos la cuenta con un saldo de 1000
        cuenta = new CuentaCorriente(1000, 0.05f);
        
        // Intentamos retirar más dinero del que hay en la cuenta (1500)
        // No debería permitir retirar más de lo que hay en la cuenta
        boolean resultado = cuenta.retirar(1500);
        
        // Verificamos que el retiro no fue posible
        assertFalse(resultado, "El retiro no debe ser posible si no hay suficiente saldo para cubrirlo");
        // Verificamos que el saldo sigue siendo 1000
        assertEquals(1000, cuenta.saldo, "El saldo no debe cambiar si el retiro es insuficiente");
        // Verificamos que no haya sobregiro
        assertEquals(0, cuenta.sobregiro, "El sobregiro no debe ser generado si el retiro es insuficiente");
    }
    
    }

