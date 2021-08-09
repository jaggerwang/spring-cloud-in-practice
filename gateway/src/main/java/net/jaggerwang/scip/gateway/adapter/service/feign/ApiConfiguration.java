package net.jaggerwang.scip.gateway.adapter.service.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import net.jaggerwang.scip.common.adapter.service.feign.ApiResultDecoder;
import org.springframework.context.annotation.Bean;
import reactivefeign.client.ReactiveHttpRequestInterceptor;

/**
 * @author Jagger Wang
 */
public class ApiConfiguration {
    @Bean
    public Decoder decoder(ObjectMapper objectMapper) {
        return new ApiResultDecoder(objectMapper);
    }

    @Bean
    public ReactiveHttpRequestInterceptor reactiveHttpRequestInterceptor() {
        return new UserIdRequestInterceptor();
    }
}
