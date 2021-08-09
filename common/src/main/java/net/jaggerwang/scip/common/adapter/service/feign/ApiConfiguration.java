package net.jaggerwang.scip.common.adapter.service.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;

/**
 * @author Jagger Wang
 */
public class ApiConfiguration {
    @Bean
    public Decoder decoder(ObjectMapper objectMapper) {
        return new ApiResultDecoder(objectMapper);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new UserIdRequestInterceptor();
    }
}
