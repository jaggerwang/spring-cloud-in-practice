# Spring Cloud in Practice

This project can be used as a starter for spring cloud microservice application developing. It uses [Spring Cloud Consul](https://cloud.spring.io/spring-cloud-consul/reference/html/) for service discovery and config management, [Spring Cloud Gateway](https://cloud.spring.io/spring-cloud-gateway/reference/html/) to implement api gateway, and [Keycloak](https://www.keycloak.org/) for running an optional OAuth2 service. There is also an article [Spring Cloud 微服务开发指南](https://blog.jaggerwang.net/spring-cloud-micro-service-develop-tour/) for learning this project.

## Dependent frameworks and packages

1. [Spring Boot](https://spring.io/projects/spring-boot) Web framework and server
1. [Spring Data JPA](https://spring.io/projects/spring-data-jpa) Access database
1. [Querydsl JPA](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa) Type safe dynamic sql builder
1. [Spring Security](https://spring.io/projects/spring-security) Authenticate and authrorize
1. [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) Api gateway
1. [Spring Cloud Consul](https://spring.io/projects/spring-cloud-consul) Service discovery
1. [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign) Declarative rest client

## Architecture

![spring-cloud-microservice-architecture](https://user-images.githubusercontent.com/1255011/128450568-40931a62-f4c5-4e80-afd4-89a383d857f6.png)

1. Authentication checked and implemented in gateway, it will request user info from user service to verify username and password, and write logged user info into session storage.
1. Gateway will pass logged user id through header `X-User-Id`, and it will be passed between microservices.
1. Gateway also support login with thirdparty OAuth2 service, after successfully completed the OAuth2 authentication flow, it will request user service to bind thirdparty user to an inner user. Other microservices only knows about the inner user, this makes user related logic keeps the same for all microservices.  

### Microservices

| Name  | Description |
| ------------- | ------------- |
| Gateway | Request routing, authentication and authorization checking |
| User | User related business, including users management, roles management and following relationship |
| Post | Post related business |
| File | File related business |
| Stat | Stat related business |

## APIs

| Path  | Method | Description |
| ------------- | ------------- | ------------- |
| /auth/login | POST | Login user |
| /auth/logout | GET | Logout user |
| /auth/logged | GET | Logged user |
| /user/user/register | POST | Register user |
| /user/user/modify | POST | Modify logged user |
| /user/user/info | GET | Get user info |
| /user/user/sendMobileVerifyCode | POST | Send mobile verify code |
| /user/user/sendEmailVerifyCode | POST | Send email verify code |
| /user/follow/follow | POST | Follow user |
| /user/follow/unfollow | POST | Unfollow user |
| /user/follow/following | GET | Following users of someone |
| /user/follow/follower | GET | Fans of some user |
| /post/post/publish | POST | Publish post |
| /post/post/delete | POST | Delete post |
| /post/post/info | GET | Get post info |
| /post/post/published | GET | Get published posts of some user |
| /post/post/like | POST | Like post |
| /post/post/unlike | POST | Unlike post |
| /post/post/liked | GET | Liked posts of some user |
| /post/post/following | GET | Posts published by following users of someone |
| /file/file/upload | POST | Upload file |
| /file/file/info | GET | Get file meta info |
| /stat/stat/ofUser | GET | Get user stat info |
| /stat/stat/ofPost | GET | Get post stat info |

The path is following the format `/<service>/<module>/<operation>`, and the `/<service>` prefix will be stripped away when gateway forwarding request to microservices.

## How to run

This project need java 11+.

### By local environment

#### Run a mysql service

If you use macOS, you can use `brew install mysql` to install mysql, and use `brew services start mysql` to start service at port `3306`. Then you should create databases and tables for each microservice using sql files under `db/migration/mysql`.

1. Use `V1__Initial_create_dbs.sql` to create databases and accounts to access these databases;
1. Use `V2__Initial_create_tables.sql` to create tables;
1. \[Optional\] Use `V3__Initial_insert_data.sql` to insert some data for testing;

#### Run a redis service

If you use macOS, you can use `brew install redis` to install redis, and use `brew services start redis` to start service at port `6379`.

#### Run a consul service

If you use macOS, you can use `brew install consul` to install consul, and use `brew services start consul` to start service at port `8500`.

#### Package all microservices

```bash
./mvnw package
```

#### Start all microservices

```bash
java -jar gateway/target/spring-cloud-in-practice-gateway-1.0.0-SNAPSHOT.jar
java -jar user/target/spring-cloud-in-practice-user-1.0.0-SNAPSHOT.jar
java -jar post/target/spring-cloud-in-practice-post-1.0.0-SNAPSHOT.jar
java -jar file/target/spring-cloud-in-practice-file-1.0.0-SNAPSHOT.jar
java -jar stat/target/spring-cloud-in-practice-stat-1.0.0-SNAPSHOT.jar
```

Then you can access all apis through gateway at `http://localhost:8080`.

### By docker compose

#### Package all services

```bash
./mvnw package
```

#### Start all services

```bash
docker-compose up
```

If you repackaged services, you should add `--build` option to rebuild images.

Then you can access all apis at `http://localhost:8080`.

## Login with OAuth2 service

### Choose an OAuth2 service and register a client

You can choose any OAuth2 service like [GitHub](https://github.com/settings/developers) or [Google](https://developers.google.com/identity/protocols/OpenIDConnect), or you can start your own OAuth2 service using open source software like [Keycloak](https://www.baeldung.com/spring-boot-keycloak), you can even [Embed Keycloak in a Spring Boot Application](https://www.baeldung.com/keycloak-embedded-in-spring-boot-app). Here we choose to using Keycloak, and register a client in consistent with configuration at `spring.security.oauth2.client` in `gateway/src/main/resources/application.yml`, under realm `JW`. 

Some import attributes of the registered client are as follows:
- Client ID: scip
- Client Protocol: openid-connect
- Access Type: confidential
- Client Roles: user post file stat

Create a user named `jaggerwang` with password `123456` for testing, and given all roles of client `scip`.

### Initiate an OAuth2 authentication flow

You can now open `http://localhost:8080/login` to initiate an OAuth2 authorization code flow and logout at endpoint `/logout`.
