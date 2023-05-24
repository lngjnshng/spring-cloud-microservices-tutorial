# Getting Started

A Basic Spring Cloud Microservice Sample Project

* Java 17
* Spring boot 3.0.6
* Spring cloud 2022.0.2
* Eureka Discovery Server
* Spring Cloud Gateway
* Swagger 3

## Start Servers
### Start Eureka Server
```
cd discovery
mvn clean test-compile spring-boot:run
```
### Start API Gateway Server
```
cd gateway
mvn clean test-compile spring-boot:run
```
### Start Authentication Server
```
cd auth
mvn clean test-compile spring-boot:run
```
### Start Order Server
```
cd order
mvn clean test-compile spring-boot:run
```
## Access & Tests
### Access Eureka web page
```
http://localhost:8081
```
### Login
```
curl --location --request POST 'http://localhost/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"admin",
    "password": "password"
}'
```
### Query order by order ID
```
curl --location --request GET 'http://localhost/api/order/1' \
--header 'Authorization: Bearer eyJhbGciOi...Ubn-VZlPmH8gxA' 
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