package net.jaggerwang.scip.gateway.api.config;

import com.coxautodev.graphql.tools.SchemaParserOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.Assert;
import graphql.language.*;
import graphql.schema.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class GraphQLConfig {
    @Bean
    public SchemaParserOptions schemaParserOptions(ObjectMapper objectMapper) {
        return SchemaParserOptions.newOptions()
                .objectMapperProvider(fieldDefinition -> objectMapper).build();
    }

    @Bean
    public GraphQLScalarType jsonType() {
        return GraphQLScalarType.newScalar().name("JSON").description("A json scalar")
                .coercing(new Coercing<Object, Object>() {
                    @Override
                    public Object serialize(Object input) throws CoercingSerializeException {
                        return input;
                    }

                    @Override
                    public Object parseValue(Object input) throws CoercingParseValueException {
                        return input;
                    }

                    @Override
                    public Object parseLiteral(Object input) throws CoercingParseLiteralException {
                        return parseLiteral(input, Collections.emptyMap());
                    }

                    @Override
                    public Object parseLiteral(Object input, Map<String, Object> variables)
                            throws CoercingParseLiteralException {
                        if (!(input instanceof Value)) {
                            throw new CoercingParseLiteralException(
                                    "Expected AST type 'StringValue' but was '"
                                            + (input == null ? "null"
                                                    : input.getClass().getSimpleName())
                                            + "'.");
                        }
                        if (input instanceof NullValue) {
                            return null;
                        }
                        if (input instanceof FloatValue) {
                            return ((FloatValue) input).getValue();
                        }
                        if (input instanceof StringValue) {
                            return ((StringValue) input).getValue();
                        }
                        if (input instanceof IntValue) {
                            return ((IntValue) input).getValue();
                        }
                        if (input instanceof BooleanValue) {
                            return ((BooleanValue) input).isValue();
                        }
                        if (input instanceof EnumValue) {
                            return ((EnumValue) input).getName();
                        }
                        if (input instanceof VariableReference) {
                            var varName = ((VariableReference) input).getName();
                            return variables.get(varName);
                        }
                        if (input instanceof ArrayValue) {
                            var values = ((ArrayValue) input).getValues();
                            return values.stream().map(v -> parseLiteral(v, variables))
                                    .collect(Collectors.toList());
                        }
                        if (input instanceof ObjectValue) {
                            var values = ((ObjectValue) input).getObjectFields();
                            var parsedValues = new LinkedHashMap<String, Object>();
                            values.forEach(fld -> {
                                var parsedValue = parseLiteral(fld.getValue(), variables);
                                parsedValues.put(fld.getName(), parsedValue);
                            });
                            return parsedValues;
                        }
                        return Assert.assertShouldNeverHappen("We have covered all Value types");
                    }
                }).build();
    }
}
