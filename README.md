#repositorio GIT
https://github.com/juangabriel1587/test-java-devsu.git


# test-java-devsu


construido sobre java 17

# construir jar 
mvn clean package -DskipTests -DfinalName=cliente-persona-service

-------------------------------SERVICIO ---clientes-personas-service
#DOCKER BASE DE DATOS
docker network create test-bp

docker run -d --name postgres-db --network test-bp -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=bankingbp -p 5432:5432 postgres:16


#Constriur imagen 
docker build -t clientes-personas-service .

#run
docker run -d --name cliente-persona-service --network test-bp -p 8081:8081 cliente-persona-service


al correr por primera vez se crearan las tablas 

-------------------------------SERVICIO ---cuentas-movimientos-service


construido sobre java 17
# construir jar 
mvn clean package -DskipTests -DfinalName=cuentas-movimientos-service

#crear red interna de docker 
docker network create test-bp

#DOCKER BASE DE DATOS
#docker run -d --name postgres-db --network test-bp -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=bankingbp -p 5432:5432 postgres:16


#Constriur imagen 
docker build -t cuentas-movimientos-service .

#run
docker run -d --name cuentas-movimientos-service --network test-bp -p 8082:8082 cuentas-movimientos-service

#docker logs cuentas-movimientos-service
#docker stop cuentas-movimientos-service
#docker rm cuentas-movimientos-service


al correr por primera vez se crearan las tablas 