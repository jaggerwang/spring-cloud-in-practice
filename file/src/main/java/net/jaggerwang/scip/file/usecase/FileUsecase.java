package net.jaggerwang.scip.file.usecase;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.jaggerwang.scip.common.usecase.exception.*;
import net.jaggerwang.scip.common.entity.FileEntity;
import net.jaggerwang.scip.file.usecase.port.dao.FileDAO;
import net.jaggerwang.scip.file.usecase.port.service.StorageService;

public class FileUsecase {
    private FileDAO fileDAO;

    private StorageService storageService;

    public FileUsecase(FileDAO fileDAO, StorageService storageService) {
        this.fileDAO = fileDAO;
        this.storageService = storageService;
    }

    public FileEntity upload(String path, byte[] content, FileEntity fileEntity) {
        String savedPath;
        try {
            savedPath = storageService.store(path, content, fileEntity.getMeta());
        } catch (IOException e) {
            throw new UsecaseException("存储文件出错");
        }

        var file = FileEntity.builder().userId(fileEntity.getUserId())
                .region(fileEntity.getRegion()).bucket(fileEntity.getBucket()).path(savedPath)
                .meta(fileEntity.getMeta()).build();
        return fileDAO.save(file);
    }

    public Optional<FileEntity> info(Long id) {
        return fileDAO.findById(id);
    }

    public List<FileEntity> infos(List<Long> ids, Boolean keepNull) {
        var fileEntities = fileDAO.findAllById(ids);

        if (!keepNull) {
            fileEntities.removeIf(Objects::isNull);
        }

        return fileEntities;
    }
}
