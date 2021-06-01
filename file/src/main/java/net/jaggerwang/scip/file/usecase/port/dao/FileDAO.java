package net.jaggerwang.scip.file.usecase.port.dao;

import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.entity.FileBO;

public interface FileDAO {
    FileBO save(FileBO fileBO);

    Optional<FileBO> findById(Long id);

    List<FileBO> findAllById(List<Long> ids);
}
