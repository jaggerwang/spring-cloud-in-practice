package net.jaggerwang.scip.common.adapter.service.feign;

import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;

/**
 * @author Jagger Wang
 */
public class ApiConfiguration {

    @Bean
    public Decoder apiResultDecoder() {
        return new ApiResultDecoder();
    }
}
