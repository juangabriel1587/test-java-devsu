Feature: Crear una cuenta

  Scenario: Crear una cuenta bancaria
    Given url 'http://localhost:8082/cuentas'
    And request cuenta
    When method post
    Then status 201
    And match response.numeroCuenta == cuenta.numeroCuenta
    And match response.tipoCuenta == "Ahorros"
    And match response.saldoInicial == 2000
