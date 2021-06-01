package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;

import java.util.List;

public interface FileSyncService {
    FileDTO info(Long id);

    List<FileDTO> infos(List<Long> ids, Boolean keepNull);
}
