package net.jaggerwang.scip.file.usecase.port.repository;

import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.entity.FileEntity;

public interface FileRepository {
    FileEntity save(FileEntity fileEntity);

    Optional<FileEntity> findById(Long id);

    List<FileEntity> findAllById(List<Long> ids);
}
