package net.jaggerwang.scip.common.adapter.service.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;

/**
 * @author Jagger Wang
 */
public class ApiConfiguration {
    @Bean
    public Decoder apiResultDecoder(ObjectMapper objectMapper) {
        return new ApiResultDecoder(objectMapper);
    }

    @Bean
    public UserIdRequestInterceptor userIdRequestInterceptor() {
        return new UserIdRequestInterceptor();
    }
}
