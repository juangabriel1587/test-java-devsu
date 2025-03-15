function fn() {
    var port = karate.properties['karate.port'] || 8080; // Si no se pasa el puerto, usa 8080 por defecto
    return { baseUrl: 'http://localhost:' + port };
}
