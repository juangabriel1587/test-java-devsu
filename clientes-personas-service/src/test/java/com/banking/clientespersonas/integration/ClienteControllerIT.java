package com.banking.clientespersonas.integration;

import com.banking.clientespersonas.models.Cliente;
import com.banking.clientespersonas.repositories.ClienteRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import io.restassured.path.json.JsonPath;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClienteControllerIT {

    @LocalServerPort
    private int port;

    private static Cliente cliente;
    private static Long clienteId; 
    private static String NOMBRE_PERSONA ="Carlos tress";


    @BeforeAll
    static void setUp() {
    	 cliente = new Cliente();
    	    cliente.setNombre(NOMBRE_PERSONA);
    	    cliente.setIdentidad(UUID.randomUUID().toString().substring(0, 10)); 
    	    cliente.setIdentificacion(UUID.randomUUID().toString().substring(0, 10));
    	    cliente.setContrasena("1234");
    	    cliente.setDireccion("Otavalo s/n y principal");
    	    cliente.setTelefono("098254785");
    	    cliente.setEdad(45);
    	    cliente.setGenero("Masculino");
    	    cliente.setEstado(true);
    }



    @Test
    @Order(1)
    @DisplayName("Crear un Cliente")
    void crearCliente() {
        String responseBody = 
            given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(cliente)
            .when()
                .post("/clientes")
            .then()
                .statusCode(201) 
                .body("nombre",equalTo(NOMBRE_PERSONA))
                .extract()
                .asString(); 

        System.out.println("JSON de respuesta: " + responseBody); 

      
        JsonPath jsonPath = JsonPath.from(responseBody);
        clienteId = jsonPath.getLong("id");  // Convert to Long
    
        Assertions.assertNotNull(clienteId, "El cliente ID no debe ser nulo");
    }

    @Test
    @Order(2)
    @DisplayName("Obtener Cliente por ID")
    void obtenerClientePorId() {
        Assertions.assertNotNull(clienteId, "El cliente ID debe estar inicializado");

        given()
            .port(port)
            .contentType(ContentType.JSON)
        .when()
            .get("/clientes/" + clienteId)  
        .then()
            .statusCode(200)
            .body("nombre", equalTo(NOMBRE_PERSONA));
    }

    @Test
    @Order(3)
    @DisplayName("Eliminar Cliente")
    void eliminarCliente() {
        Assertions.assertNotNull(clienteId, "El cliente ID debe estar inicializado");

        given()
            .port(port)
            .contentType(ContentType.JSON)
        .when()
            .delete("/clientes/" + clienteId)  
        .then()
            .statusCode(200)
            .body(equalTo("Cliente eliminado correctamente con ID: " + clienteId));
    }
    
    @AfterAll
    static void tearDown() {
        System.out.println("Finalizando pruebas...");
    }
}
