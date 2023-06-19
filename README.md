# Getting Started

A Basic Spring Cloud Microservice Sample Project

* Java 17
* Spring boot 3.0.6
* Spring cloud 2022.0.2
* Consul Discovery Server
* Spring Cloud Gateway
* Swagger 3

## Start Servers
### Start Consul Server with Docker Compose
```
cd consul
docker-compose -f cluster.yaml up -d
```
### Stop Consul Server
```
cd consul
docker-compose -f cluster.yaml down
```
### Start Consul Server in Windows for develop
```
consul agent -dev
```
### Start API Gateway Server
```
cd gateway
mvn clean test-compile spring-boot:run
```
### Start Order Server
```
cd order
mvn clean test-compile spring-boot:run
```
## Access & Tests
### Access consul web page
```
http://localhost:8500
```
### Query order by order ID
```
curl http://localhost/api/order/1
```
### Swagger
To enable swagger, please set the environment variable ENV as dev before running the server
```
export ENV=dev
```
To access the swagger UI
```
http://localhost/swagger-ui.html
```