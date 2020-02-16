package net.jaggerwang.scip.gateway.api.config;

import graphql.Assert;
import graphql.GraphQL;
import graphql.language.*;
import graphql.scalars.ExtendedScalars;
import graphql.schema.*;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import net.jaggerwang.scip.gateway.adapter.graphql.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Configuration(proxyBeanMethods = false)
public class GraphQLConfig {
    private GraphQL graphQL;

    @Value("classpath:schema.graphqls")
    private Resource schema;

    QueryDataFetchers queryDataFetchers;
    MutationDataFetchers mutationDataFetchers;
    UserDataFetcher userDataFetchers;
    PostDataFetcher postDataFetchers;
    FileDataFetchers fileDataFetchers;
    UserStatDataFetcher userStatDataFetchers;
    PostStatDataFetcher postStatDataFetchers;

    public GraphQLConfig(QueryDataFetchers queryDataFetchers,
                         MutationDataFetchers mutationDataFetchers,
                         UserDataFetcher userDataFetchers,
                         PostDataFetcher postDataFetchers,
                         FileDataFetchers fileDataFetchers,
                         UserStatDataFetcher userStatDataFetchers,
                         PostStatDataFetcher postStatDataFetchers) {
        this.queryDataFetchers = queryDataFetchers;
        this.mutationDataFetchers = mutationDataFetchers;
        this.userDataFetchers = userDataFetchers;
        this.postDataFetchers = postDataFetchers;
        this.fileDataFetchers = fileDataFetchers;
        this.userStatDataFetchers = userStatDataFetchers;
        this.postStatDataFetchers = postStatDataFetchers;
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        var reader = new InputStreamReader(schema.getInputStream(), StandardCharsets.UTF_8);
        var sdl = FileCopyUtils.copyToString(reader);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        var typeRegistry = new SchemaParser().parse(sdl);
        var runtimeWiring = buildWiring();
        var schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring().scalar(ExtendedScalars.Json)
                .type(newTypeWiring("Query").dataFetchers(queryDataFetchers.toMap()))
                .type(newTypeWiring("Mutation").dataFetchers(mutationDataFetchers.toMap()))
                .type(newTypeWiring("User").dataFetchers(userDataFetchers.toMap()))
                .type(newTypeWiring("Post").dataFetchers(postDataFetchers.toMap()))
                .type(newTypeWiring("File").dataFetchers(fileDataFetchers.toMap()))
                .type(newTypeWiring("UserStat").dataFetchers(userStatDataFetchers.toMap()))
                .type(newTypeWiring("PostStat").dataFetchers(postStatDataFetchers.toMap()))
                .build();
    }
}
