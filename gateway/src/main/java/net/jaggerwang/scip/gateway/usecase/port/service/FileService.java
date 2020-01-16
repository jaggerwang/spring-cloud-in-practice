package net.jaggerwang.scip.gateway.usecase.port.service;

import net.jaggerwang.scip.gateway.usecase.port.service.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FileService {
    Mono<List<FileDto>> upload(String region, String bucket, String path, List<MultipartFile> files);

    Mono<List<FileDto>> upload(List<MultipartFile> files);

    Mono<FileDto> info(Long id);

    Mono<List<FileDto>> infos(List<Long> ids, Boolean keepNull);
}
