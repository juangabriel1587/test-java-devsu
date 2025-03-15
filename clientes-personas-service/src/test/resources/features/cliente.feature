Feature: Pruebas de Cliente API

  Scenario: Crear un nuevo cliente
    Given url 'http://localhost:8081/clientes'
    And request { "nombre": "Carlos Tres", "identidad": "JP0015", "identificacion": "1789098776", "contrasena": "1234", "direccion": "Otavalo s/n y principal", "estado": true }
    When method POST
    Then status 201
    And match response.nombre == "Carlos Tres"
    And match response.identificacion == "1789098776"

    Scenario: Obtener un cliente por ID
    Given path 'clientes', 5
    When method GET
    Then status 200
    And match response.nombre == "Carlos tres"