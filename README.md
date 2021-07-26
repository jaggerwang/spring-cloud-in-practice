# Spring Cloud in Practice

This project can be used as a starter for spring cloud microservice application developing. It uses [Spring Cloud Consul](https://cloud.spring.io/spring-cloud-consul/reference/html/) for service discovery and config management, [Spring Cloud Gateway](https://cloud.spring.io/spring-cloud-gateway/reference/html/) to implement api gateway, and [ORY/Hydra](https://github.com/ory/hydra) for running an optional OAuth2 server. There is also an article [Spring Cloud 微服务开发指南](https://blog.jaggerwang.net/spring-cloud-micro-service-develop-tour/) for learning this project.

## Dependent frameworks and packages

1. [Spring Boot](https://spring.io/projects/spring-boot) Web framework and server
1. [Spring Data JPA](https://spring.io/projects/spring-data-jpa) Access database
1. [Querydsl JPA](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa) Type safe dynamic sql builder
1. [Spring Security](https://spring.io/projects/spring-security) Authenticate and authrorize
1. [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) Api gateway
1. [Spring Cloud Consul](https://spring.io/projects/spring-cloud-consul) Service discovery
1. [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign) Declarative rest client
1. [ORY/Hydra](https://github.com/ory/hydra) OAuth2 server

## Architecture

![spring-cloud-microservice-architecture](https://user-images.githubusercontent.com/1255011/126436826-9699a641-1d64-4262-aa32-bcfed99c5b5c.png)

1. Authentication checked and implemented in gateway, it will request user info from auth service.
1. Gateway will pass current user identity through header `X-User-Id`, and it will be passed between microservices.

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
| /login | POST | Login user |
| /logout | GET | Logout user |
| /logged | GET | Logged user |
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

The path is following the format `/<service>/<module>/<operation>`, and the `/<service>` prefix will be stripped when gateway forwarding request to backend services.

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

## Login with OAuth2 Service

This application also support login with OAuth2 service, you need switch to `oauth2` branch to experience this feature. In the following, we will use [ORY/Hydra](https://github.com/ory/hydra) to running an OAuth2 service.

### Architecture

![spring-cloud-microservice-architecture-with-oauth2](https://user-images.githubusercontent.com/1255011/126439917-13fb74a6-a2c3-4646-81fd-e222ba06dc83.png)

1. Gateway will request oauth2 service to authenticate user when needed.
1. Hydra oauth2 service need auth service (through gateway) to manage user, this allows you to use your own user management. If you use other oauth2 service with user management like [Keycloak](https://www.keycloak.org/), you can remove auth service.

### Install hydra

You need install `hydra` first. You can use the following commands to install hydra on macOS:

```bash
brew tap ory/hydra
brew install ory/hydra/hydra
``` 

### Prepare database and start hydra service

Connect to your mysql service and create a database for hydra.

```sql
CREATE DATABASE `scip_hydra`;
```

Then use the following commands to init database and run hydra service.

```bash
DSN=mysql://root:@tcp(localhost:3306)/scip_hydra hydra migrate sql -e --yes

STRATEGIES_ACCESS_TOKEN=jwt LOG_LEVEL=info SECRETS_SYSTEM=a2N4m0XL659TIrB2V3fJBxUED5Zv5zUQ DSN=mysql://scip_hydra:123456@tcp(localhost:3306)/scip_hydra URLS_SELF_ISSUER=http://localhost:4444/ URLS_LOGIN=http://localhost:8080/auth/hydra/login URLS_CONSENT=http://localhost:8080/auth/hydra/consent URLS_LOGOUT=http://localhost:8080/auth/hydra/logout TTL_ACCESS_TOKEN=8h TTL_REFRESH_TOKEN=720h hydra serve all --dangerous-force-http --dangerous-allow-insecure-redirect-urls 'http://localhost:8080/auth/hydra/login,http://localhost:8080/auth/hydra/consent,http://localhost:8080/auth/hydra/logout'
```

### Create OAuth2 clients

We will create two clients, one is for hydra's builtin client, and the other is for this project.

```bash
hydra --endpoint 'http://localhost:4445/' clients create --id test --name 'Test' --secret E0g8oR7m711bGcvy --grant-types authorization_code,refresh_token,client_credentials,implicit --response-types token,code,id_token --scope openid,offline,profile --callbacks 'http://localhost:4446/callback'

hydra --endpoint 'http://localhost:4445/' clients create --id scip --name 'Spring Cloud in Practice' --secret ilxzM0AdA7BVaL7c --grant-types authorization_code,refresh_token,client_credentials,implicit --response-types token,code,id_token --scope offline,user,post,file,stat --callbacks 'http://localhost:8080/login/oauth2/code/hydra'
```

### Test OAuth2 login with hydra's builtin client

```bash
hydra token user --auth-url 'http://localhost:4444/oauth2/auth' --token-url 'http://localhost:4444/oauth2/token' --client-id test --client-secret E0g8oR7m711bGcvy --scope openid,offline,profile --redirect 'http://localhost:4446/callback'
```

It will auto open `http://localhost:4446` to commence OAuth2 authorize flow.

### Test OAuth2 login with scip client

Open `http://localhost:8080/login` to commence OAuth2 authorization flow.
