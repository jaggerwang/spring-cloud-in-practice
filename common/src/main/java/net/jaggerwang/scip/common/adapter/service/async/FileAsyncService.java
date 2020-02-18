package net.jaggerwang.scip.common.adapter.service.async;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileAsyncService extends InternalAsyncService implements net.jaggerwang.scip.common.usecase.port.service.async.FileAsyncService {
    public FileAsyncService(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory,
                            ObjectMapper objectMapper) {
        super(webClient, cbFactory, objectMapper);
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
        return getData("/file/infos", Map.of(
                    "id", String.join(",", ids.stream()
                        .map(id -> id.toString())
                        .collect(Collectors.toList())),
                    "keepNull", keepNull.toString()))
                .map(data -> objectMapper.convertValue(data.get("files"), new TypeReference<>(){}));
    }
}
