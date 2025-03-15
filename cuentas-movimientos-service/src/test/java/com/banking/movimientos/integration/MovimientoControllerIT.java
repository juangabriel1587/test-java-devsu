package com.banking.movimientos.integration;

import com.banking.movimientos.models.Cuenta;
import com.banking.movimientos.models.Movimiento;
import com.banking.movimientos.models.TipoMovimiento;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) 
class MovimientoControllerIT {

    @LocalServerPort
    private int port;

    private Cuenta cuenta;

    @BeforeAll
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        cuenta = new Cuenta();
        cuenta.setNumeroCuenta(String.valueOf(System.currentTimeMillis())); 
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(new BigDecimal("2000.00"));
        cuenta.setEstado(true);
        cuenta.setClienteId(4L); 
    }

    @Test
    @Order(1)
    void testCrearCuenta() {
      
        cuenta = given()
                .contentType(ContentType.JSON)
                .body(cuenta)
            .when()
                .post("/cuentas")
            .then()
                .statusCode(201)
                .body("numeroCuenta", equalTo(cuenta.getNumeroCuenta()))
                .body("tipoCuenta", equalTo("Ahorros"))
                .body("saldoInicial", comparesEqualTo(2000.00f))
                .extract().as(Cuenta.class);
    }

    @Test
    @Order(2)
    void testRegistrarDeposito() {
        Movimiento deposito = new Movimiento();
        deposito.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        deposito.setFecha(LocalDateTime.now());
        deposito.setValor(new BigDecimal("500.00"));
        deposito.setCuenta(cuenta);

        given()
            .contentType(ContentType.JSON)
            .body(deposito)
        .when()
            .post("/movimientos")
        .then()
            .statusCode(201)
            .body("saldoDisponible", comparesEqualTo(2500.00f)); 
    }

    @Test
    @Order(3)
    void testRegistrarRetiroSaldoSuficiente() {
        Movimiento retiro = new Movimiento();
        retiro.setTipoMovimiento(TipoMovimiento.RETIRO);
        retiro.setFecha(LocalDateTime.now());
        retiro.setValor(new BigDecimal("500.00"));
        retiro.setCuenta(cuenta);

        given()
            .contentType(ContentType.JSON)
            .body(retiro)
        .when()
            .post("/movimientos")
        .then()
            .statusCode(201)
            .body("saldoDisponible", comparesEqualTo(2000.00f)); 
    }

    @Test
    @Order(4)
    void testRegistrarRetiroSaldoInsuficiente() {
        Movimiento retiro = new Movimiento();
        retiro.setTipoMovimiento(TipoMovimiento.RETIRO);
        retiro.setFecha(LocalDateTime.now());
        retiro.setValor(new BigDecimal("3000.00")); 
        retiro.setCuenta(cuenta);

        given()
            .contentType(ContentType.JSON)
            .body(retiro)
        .when()
            .post("/movimientos")
        .then()
            .statusCode(400)
            .body(containsString("Saldo no disponible"));
    }

    @Test
    @Order(5)
    void testObtenerMovimientos() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/movimientos/cuenta/{idCuenta}", cuenta.getId()) 
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0)); // 
    }
}
