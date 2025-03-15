package com.banking.clientespersonas.integration;

import com.intuit.karate.junit5.Karate;

class ClienteTest {
    
    @Karate.Test
    Karate testClienteAPI() {
        return Karate.run("classpath:features/cliente.feature").relativeTo(getClass());
    }
}