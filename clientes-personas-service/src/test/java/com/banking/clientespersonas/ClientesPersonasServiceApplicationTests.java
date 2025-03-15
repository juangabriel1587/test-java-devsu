package com.banking.clientespersonas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ClientesPersonasServiceApplicationTests {

    @Test
    void contextLoads() {
    	  System.out.println("La prueba contextLoads() se ejecutó correctamente.");
        assertThat(true).isTrue();
    }
}
