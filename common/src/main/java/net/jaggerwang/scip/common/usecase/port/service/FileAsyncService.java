package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FileAsyncService {
    Mono<List<FileDTO>> upload(String region, String bucket, String path, List<MultipartFile> files);

    Mono<List<FileDTO>> upload(List<MultipartFile> files);

    Mono<FileDTO> info(Long id);

    Mono<List<FileDTO>> infos(List<Long> ids, Boolean keepNull);
}
