package net.jaggerwang.scip.common.usecase.port.service.async;

import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FileAsyncService {
    Mono<List<FileDto>> upload(String region, String bucket, String path, List<MultipartFile> files);

    Mono<List<FileDto>> upload(List<MultipartFile> files);

    Mono<FileDto> info(Long id);

    Mono<List<FileDto>> infos(List<Long> ids, Boolean keepNull);
}
