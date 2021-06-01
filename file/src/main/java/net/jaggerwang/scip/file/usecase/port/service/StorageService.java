package net.jaggerwang.scip.file.usecase.port.service;

import java.io.IOException;

import net.jaggerwang.scip.common.entity.FileBO;

public interface StorageService {
    String store(String path, byte[] content, FileBO.Meta meta) throws IOException;
}
