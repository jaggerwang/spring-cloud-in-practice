package net.jaggerwang.scip.gateway.adapter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.service.AsyncInternalService;
import net.jaggerwang.scip.gateway.usecase.port.service.FileService;
import net.jaggerwang.scip.gateway.usecase.port.service.dto.FileDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class FileServiceImpl extends AsyncInternalService implements FileService {
    protected ObjectMapper objectMapper;

    public FileServiceImpl(@Qualifier("fileServiceWebClient") WebClient webClient,
                           ReactiveCircuitBreakerFactory cbFactory,
                           ObjectMapper objectMapper) {
        super(webClient, cbFactory);

        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public Mono<List<FileDto>> upload(String region, String bucket, String path,
                                      List<MultipartFile> files) {
        var params = new LinkedMultiValueMap<String, Object>();
        params.add("region", region);
        params.add("bucket", bucket);
        params.add("path", path);
        params.addAll("file", files);
        return postData("/file/upload", params)
                .map(data -> objectMapper.convertValue(data.get("files"), new TypeReference<>(){}));
    }

    @Override
    public Mono<List<FileDto>> upload(List<MultipartFile> files) {
        return upload(null, null, null, files);
    }

    @Override
    public Mono<FileDto> info(Long id) {
        return getData("/file/info", Map.of("id", id.toString()))
                .map(data -> objectMapper.convertValue(data.get("file"), FileDto.class));
    }

    @Override
    public Mono<List<FileDto>> infos(List<Long> ids, Boolean keepNull) {
        return getData("/file/infos", Map.of("id", Strings.join(ids, ','), "keepNull", keepNull.toString()))
                .map(data -> objectMapper.convertValue(data.get("files"), new TypeReference<>(){}));
    }
}
