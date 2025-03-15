package com.banking.movimientos.integration;

import com.intuit.karate.junit5.Karate;

public class MovimientosTest {

	
	@Karate.Test
    Karate testMovimientosAPI() {
        return Karate.run("classpath:features/MovimientoControllerIT.feature").relativeTo(getClass());
    }
}
