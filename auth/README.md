# Getting Started

A spring cloud microservice authentication server

## Prerequisite conditions

Run discovery server at first

## To run with maven

```
mvn clean test-compile spring-boot:run
```

### Generate RSA Key pair
Generate the private key
```
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out private.key
```
Export the public key
```
openssl rsa -in private.key -pubout -out public.pub
```