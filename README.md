# Spring Cloud in Practice

This project can be used as a starter for spring cloud micro services development. It is the micro services version of [Spring Boot in Practice](https://github.com/jaggerwang/spring-boot-in-practice). It use [Spring Cloud Consul](https://cloud.spring.io/spring-cloud-consul/reference/html/) for service discovery, [Spring Cloud Gateway](https://cloud.spring.io/spring-cloud-gateway/reference/html/) to implement api gateway, and [ORY/Hydra](https://github.com/ory/hydra) for running an optional OAuth2 server. The graphql api service exposed by gateway can be used as the backend api service for this flutter app [Flutter in Practice](https://github.com/jaggerwang/flutter-in-practice). There is an article [Spring Cloud 微服务开发指南](https://blog.jaggerwang.net/spring-cloud-micro-service-develop-tour/) for learning this project.

## Dependent frameworks and packages

1. [Spring Boot](https://spring.io/projects/spring-boot) Web framework and server
1. [Spring Data JPA](https://spring.io/projects/spring-data-jpa) Access database
1. [Querydsl JPA](https://github.com/querydsl/querydsl/tree/master/querydsl-jpa) Type safe dynamic sql builder
1. [Spring Security](https://spring.io/projects/spring-security) Authenticate and authrorize
1. [GraphQL Java](https://github.com/graphql-java/graphql-java) Graphql for java
1. [Flyway](https://flywaydb.org/) Database migration
1. [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) Api gateway
1. [Spring Cloud Consul](https://spring.io/projects/spring-cloud-consul) Service discovery
1. [Spring Cloud Circuit Breaker](https://spring.io/projects/spring-cloud-circuitbreaker) Circuit breaker
1. [ORY/Hydra](https://github.com/ory/hydra) OAuth2 server

## APIs

### Rest

| Path  | Method | Description |
| ------------- | ------------- | ------------- |
| /auth/login | POST | Login |
| /auth/logout | GET | Logout |
| /auth/logged | GET | Get logged user |
| /user/register | POST | Register |
| /user/modify | POST | Modify logged user |
| /user/info | GET | Get user info |
| /user/follow | POST | Follow user |
| /user/unfollow | POST | Unfollow user |
| /user/following | GET | Following users of someone |
| /user/follower | GET | Fans of some user |
| /user/sendMobileVerifyCode | POST | Send mobile verify code |
| /post/publish | POST | Publish post |
| /post/delete | POST | Delete post |
| /post/info | GET | Get post info |
| /post/published | GET | Get published posts of some user |
| /post/like | POST | Like post |
| /post/unlike | POST | Unlike post |
| /post/liked | GET | Liked posts of some user |
| /post/following | GET | Posts of following users of someone |
| /file/upload | POST | Upload file |
| /file/info | GET | Get file meta info |

The rest api service only return top objects, not return nested objects compared to the original monolith one.

### GraphQL

```graphql
type Query {
    authLogout: User
    authLogged: User
    userInfo(id: Int!): User!
    userFollowing(userId: Int, limit: Int, offset: Int): [User!]!
    userFollowingCount(userId: Int): Int!
    userFollower(userId: Int, limit: Int, offset: Int): [User!]!
    userFollowerCount(userId: Int): Int!

    postInfo(id: Int!): Post!
    postPublished(userId: Int, limit: Int, offset: Int): [Post!]!
    postPublishedCount(userId: Int): Int!
    postLiked(userId: Int, limit: Int, offset: Int): [Post!]!
    postLikedCount(userId: Int): Int!
    postFollowing(limit: Int, beforeId: Int, afterId: Int): [Post!]!
    postFollowingCount: Int!

    fileInfo(id: Int!): File!
}

type Mutation {
    authLogin(user: UserInput!): User!
    userRegister(user: UserInput!): User!
    userModify(user: UserInput!, code: String): User!
    userSendMobileVerifyCode(type: String!, mobile: String!): String!
    userFollow(userId: Int!): Boolean!
    userUnfollow(userId: Int!): Boolean!

    postPublish(post: PostInput!): Post!
    postDelete(id: Int!): Boolean!
    postLike(postId: Int!): Boolean!
    postUnlike(postId: Int!): Boolean!
}
```

## How to run

This project need java 11+.

### By local environment

#### Run a mysql service

If you use macOS, you can use `brew install mysql` to install mysql, and use `brew services start mysql` to start service at port `3306`. Then we need to create databases for each micro service. 

```sql
CREATE DATABASE `scip_user`;
CREATE DATABASE `scip_post`;
CREATE DATABASE `scip_file`;
CREATE DATABASE `scip_stat`;
```

Suppose we can use `root` user without password to access these databases, if not you should change each application's config.

#### Run a redis service

If you use macOS, you can use `brew install redis` to install redis, and use `brew services start redis` to start service at port `6379`.

#### Run a consul service

If you use macOS, you can use `brew install consul` to install consul, and use `brew services start consul` to start service at port `8500`.

#### Package all modules

```bash
./mvnw package
```

#### Start gateway and all backend services

```bash
java -jar gateway/target/spring-cloud-in-practice-gateway-1.0.0-SNAPSHOT.jar
java -jar user/target/spring-cloud-in-practice-user-1.0.0-SNAPSHOT.jar
java -jar post/target/spring-cloud-in-practice-post-1.0.0-SNAPSHOT.jar
java -jar file/target/spring-cloud-in-practice-file-1.0.0-SNAPSHOT.jar
java -jar stat/target/spring-cloud-in-practice-stat-1.0.0-SNAPSHOT.jar
```

Then you can access all APIs at `http://localhost:8080`. For example, you can register a new user using `curl` in terminal as following:

```bash
curl --request POST 'http://localhost:8080/user/register' \
--header 'Content-Type: application/json' \
--data-raw '{
	"username": "jaggerwang",
	"password": "123456"
}'
```

### By docker compose

#### Package all modules

```bash
./mvnw package
```

#### Start all services at once

```bash
docker-compose up
```

If you repackaged any module, you should add `--build` option to enable the new jar package.

Then you can access all APIs at `http://localhost:8080`.

## OAuth2 login

This application also support OAuth2 login, you need switch to `oauth2` branch to enable this feature. We use [ORY/Hydra](https://github.com/ory/hydra) to run an OAuth2 server, and any OAuth2 client can use this service to authenticate and authorize users.

### Install hydra command

You need install `hydra` command first. You can use the following commands to install Hydra on macOS:

```bash
brew tap ory/hydra
brew install ory/hydra/hydra
``` 

### Prepare database and start Hydra server

Connect to your local mysql server and create a database for Hydra.

```sql
CREATE DATABASE `scip_auth`;
```

Then use the following commands to init database and run an OAuth2 server. 

```bash
DSN=mysql://root:@tcp(localhost:3306)/scip_auth hydra migrate sql -e --yes

STRATEGIES_ACCESS_TOKEN=jwt LOG_LEVEL=info SECRETS_SYSTEM=a2N4m0XL659TIrB2V3fJBxUED5Zv5zUQ DSN=mysql://root:@tcp(localhost:3306)/scip_auth URLS_SELF_ISSUER=http://localhost:4444/ URLS_LOGIN=http://localhost:8080/hydra/login URLS_CONSENT=http://localhost:8080/hydra/consent URLS_LOGOUT=http://localhost:8080/hydra/logout TTL_ACCESS_TOKEN=12h TTL_REFRESH_TOKEN=720h hydra serve all --dangerous-force-http --dangerous-allow-insecure-redirect-urls 'http://localhost:8080/hydra/login,http://localhost:8080/hydra/consent,http://localhost:8080/hydra/logout'
```

> This step is not needed when run by docker compose.

### Create OAuth2 clients

We will create two clients, one is for Hydra's builtin client, and the other is for the gateway service which can be used as an OAuth2 client.

```bash
hydra --endpoint 'http://localhost:4445/' clients create --id test --name 'Test' --secret E0g8oR7m711bGcvy --grant-types authorization_code,refresh_token,client_credentials,implicit --response-types token,code,id_token --scope openid,offline,profile --callbacks 'http://localhost:4446/callback'

hydra --endpoint 'http://localhost:4445/' clients create --id scip --name 'Spring Cloud in Practice' --secret ilxzM0AdA7BVaL7c --grant-types authorization_code,refresh_token,client_credentials,implicit --response-types token,code,id_token --scope offline,user,post,file,stat --callbacks 'http://localhost:8080/login/oauth2/code/hydra'
```

### Test OAuth2 login with Hydra's builtin client

```bash
hydra token user --auth-url 'http://localhost:4444/oauth2/auth' --token-url 'http://localhost:4444/oauth2/token' --client-id test --client-secret E0g8oR7m711bGcvy --scope openid,offline,profile --redirect 'http://localhost:4446/callback'
```

It will auto open `http://localhost:4446` to commence OAuth2 authorize flow.

### Test OAuth2 login with any OAuth2 client

The gateway can also be used as an OAuth2 client, open login page at `http://localhost:8080/login` to commence an OAuth2 authorization flow.

> Right now there is a bug which make OAuth2 login not working, you need remove config `exceptionHandling` in `net.jaggerwang.scip.gateway.api.config.SecurityConfig`.
