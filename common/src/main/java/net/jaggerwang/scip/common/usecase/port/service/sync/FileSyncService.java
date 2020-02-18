package net.jaggerwang.scip.common.usecase.port.service.sync;

import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;

import java.util.List;

public interface FileSyncService {
    FileDto info(Long id);

    List<FileDto> infos(List<Long> ids, Boolean keepNull);
}
