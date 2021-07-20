package net.jaggerwang.scip.common.adapter.service.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import net.jaggerwang.scip.common.usecase.exception.ApiException;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author Jagger Wang
 */
public class ApiResultDecoder implements Decoder {
    ObjectMapper objectMapper;

    public ApiResultDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (response.status() != HttpStatus.OK.value()) {
            throw new ApiException(HttpStatus.valueOf(response.status()),
                    ApiResult.Code.UNKNOWN, response.reason());
        }

        var apiResult = (ApiResult) objectMapper.readValue(
                response.body().asReader(StandardCharsets.UTF_8), objectMapper.constructType(type));
        if (apiResult.getCode() != ApiResult.Code.OK) {
            throw new ApiException(apiResult.getCode(), apiResult.getMessage());
        }

        return apiResult;
    }
}
