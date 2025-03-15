Feature: Pruebas de integración para Movimientos

  Background:
    * url 'http://localhost:8082'
    * def baseUrl = 'http://localhost:8082/movimientos'
    * def cuenta = { "numeroCuenta": "#(Math.floor(Math.random() * 1000000000).toString())", "tipoCuenta": "Ahorros", "saldoInicial": 2000, "estado": true,"clienteId": 4 }
    * def cuentaCreada = call read('crear_cuenta.feature') { cuenta: '#(cuenta)' }
    * def cuentaId = cuentaCreada.response.id

  Scenario: Registrar un depósito exitoso
    Given url baseUrl
    And request { "tipoMovimiento": "DEPOSITO", "fecha": "#(new Date().toISOString())", "valor": 500.00, "cuenta": { "id": "#(cuentaId)" } }
    When method post
    Then status 201
    And match response.saldoDisponible == 2500.0

  Scenario: Registrar un retiro con saldo suficiente
    Given url baseUrl
    And request { "tipoMovimiento": "RETIRO", "fecha": "#(new Date().toISOString())", "valor": 500.00, "cuenta": { "id": "#(cuentaId)" } }
    When method post
    Then status 201
    And match response.saldoDisponible == 1500.0

  Scenario: Registrar un retiro con saldo insuficiente
    Given url baseUrl
    And request { "tipoMovimiento": "RETIRO", "fecha": "#(new Date().toISOString())", "valor": 3000.00, "cuenta": { "id": "#(cuentaId)" } }
    When method post
    Then status 400
    And match response.message == 'Saldo no disponible'

Scenario: Obtener movimientos de una cuenta específica
    * print "Cuenta ID:", cuentaId
    Given url baseUrl + '/cuenta/' + cuentaId
    When method get
    Then status 200
    * print "Response:", response
    And match response == '#[0]'

