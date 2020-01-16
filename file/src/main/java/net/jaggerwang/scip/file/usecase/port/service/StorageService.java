package net.jaggerwang.scip.file.usecase.port.service;

import java.io.IOException;

import net.jaggerwang.scip.file.entity.FileEntity;

public interface StorageService {
    String store(String path, byte[] content, FileEntity.Meta meta) throws IOException;
}
