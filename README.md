# Spring Cloud in Practice

This project can be used as a starter for spring cloud micro services development. It is the micro services version of [Spring Boot in Practice](https://github.com/jaggerwang/spring-boot-in-practice). It use [Spring Cloud Consul](https://cloud.spring.io/spring-cloud-consul/reference/html/) for service discovery, [Spring Cloud Gateway](https://cloud.spring.io/spring-cloud-gateway/reference/html/) to implement api gateway, and [ORY/Hydra](https://github.com/ory/hydra) for running a OAuth 2.0 Provider service. There is an article [Spring Cloud 微服务开发指南](https://blog.jaggerwang.net/spring-cloud-micro-service-develop-tour/) for learning this project.

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
1. [ORY/Hydra](https://github.com/ory/hydra) OAuth 2.0 Provider

## APIs

### Rest

| Path  | Method | Description |
| ------------- | ------------- | ------------- |
| /user/register | POST | Register |
| /user/login | POST | Login |
| /user/logout | GET | Logout |
| /user/logged | GET | Get logged user |
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

### GraphQL

```graphql
type Query {
    userLogged: User
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

This project need java v11+.

### By local environment

#### Run a mysql service

If you use macOS, you can use `brew install mysql` to install mysql, and use `brew services start mysql` to start service at port `3306`. Then we need to create databases for each micro service. 

```sql
CREATE DATABASE `scip_auth`;
CREATE DATABASE `scip_user`;
CREATE DATABASE `scip_post`;
CREATE DATABASE `scip_file`;
CREATE DATABASE `scip_stat`;
```

Suppose we can use `root` user without password to access these databases, if not you should change each application's config.

#### Run a consul service

If you use macOS, you can use `brew install consul` to install consul, and use `brew services start consul` to start service at port `8500`.

#### Prepare hydra database and start hydra oauth server

We need install `hydra` command at first. You can use the following commands on macOS to install hydra.

```bash
brew tap ory/hydra
brew install ory/hydra/hydra
```

Then we can use `hydra` command to prepare database and run a OAuth 2.0 Provider service. 

```bash
DSN=mysql://root:@tcp(localhost:3306)/scip_auth hydra migrate sql -e --yes

STRATEGIES_ACCESS_TOKEN=jwt LOG_LEVEL=info SECRETS_SYSTEM=a2N4m0XL659TIrB2V3fJBxUED5Zv5zUQ DSN=mysql://root:@tcp(localhost:3306)/scip_auth URLS_SELF_ISSUER=http://localhost:4444/ URLS_LOGIN=http://localhost:8081/hydra/login URLS_CONSENT=http://localhost:8081/hydra/consent URLS_LOGOUT=http://localhost:8081/hydra/logout hydra serve all --dangerous-force-http --dangerous-allow-insecure-redirect-urls 'http://localhost:8081/hydra/login,http://localhost:8081/hydra/consent,http://localhost:8081/hydra/logout'
```

#### Create test and scip oauth client application

```bash
hydra clients create --endpoint 'http://localhost:4445/' --id test --name 'Test' --secret E0g8oR7m711bGcvy --grant-types authorization_code,refresh_token,client_credentials,implicit --response-types token,code,id_token --scope openid,offline,profile --callbacks 'http://localhost:4446/callback'

hydra clients create --endpoint 'http://localhost:4445/' --id scip --name 'Spring Cloud in Practice' --secret ilxzM0AdA7BVaL7c --grant-types authorization_code,refresh_token,client_credentials,implicit --response-types token,code,id_token --scope offline,user,post,file,stat --callbacks 'http://localhost:8080/login/oauth2/code/hydra'
```

#### Package all modules

```bash
./mvnw package
```

#### Start gateway and all micro services

```bash
java -jar gateway/target/spring-cloud-in-practice-gateway-1.0.0-SNAPSHOT.jar
java -jar gateway/target/spring-cloud-in-practice-user-1.0.0-SNAPSHOT.jar
java -jar gateway/target/spring-cloud-in-practice-post-1.0.0-SNAPSHOT.jar
java -jar gateway/target/spring-cloud-in-practice-file-1.0.0-SNAPSHOT.jar
java -jar gateway/target/spring-cloud-in-practice-stat-1.0.0-SNAPSHOT.jar
```

#### Register a test user

```bash
curl --request POST 'http://localhost:8080/user/register' \
--header 'Content-Type: application/json' \
--data-raw '{
	"username": "jaggerwang",
	"password": "123456"
}'
```

#### Start test client application

The test client application is only for testing OAuth 2.0 authentication flow.

```bash
hydra token user --auth-url 'http://localhost:4444/oauth2/auth' --token-url 'http://localhost:4444/oauth2/token' --client-id test --client-secret E0g8oR7m711bGcvy --scope openid,offline,profile --redirect 'http://localhost:4446/callback'
```

It will auto open `http://localhost:4446` for testing OAuth 2.0 Authorize Code Flow. And you can access all apis through gateway `http://localhost:8080`.

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

#### Create test and scip oauth client application

```bash
hydra clients create --endpoint 'http://localhost:9091/' --id test --secret E0g8oR7m711bGcvy --grant-types authorization_code,refresh_token,client_credentials,implicit --response-types token,code,id_token --scope openid,offline,profile --callbacks 'http://localhost:4446/callback'

hydra clients create --endpoint 'http://localhost:9091/' --id scip --name 'Spring Cloud in Practice' --secret ilxzM0AdA7BVaL7c --grant-types authorization_code,refresh_token,client_credentials,implicit --response-types token,code,id_token --scope offline,user,post,file,stat --callbacks 'http://localhost:9080/login/oauth2/code/hydra'
```

#### Register a test user

```bash
curl --request POST 'http://localhost:9080/user/register' \
--header 'Content-Type: application/json' \
--data-raw '{
	"username": "jaggerwang",
	"password": "123456"
}'
```

#### Start test client application

The test client application is only for testing OAuth 2.0 authentication flow.

```bash
hydra token user --auth-url 'http://localhost:9090/oauth2/auth' --token-url 'http://localhost:9090/oauth2/token' --client-id test --client-secret E0g8oR7m711bGcvy --scope openid,offline,profile --redirect 'http://localhost:4446/callback'
```

It will auto open `http://localhost:4446` for testing OAuth 2.0 Authorize Code Flow. And you access all apis through gateway `http://localhost:9080`.