package net.jaggerwang.scip.file.adapter.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.jaggerwang.scip.common.util.generator.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.common.entity.FileBO.Meta;
import net.jaggerwang.scip.file.usecase.port.service.StorageService;

@Component
public class LocalStorageService implements StorageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private IdGenerator idGenerator = new IdGenerator();

    @Override
    public String store(String path, byte[] content, Meta meta) throws IOException {
        var saveDir = Paths.get(uploadDir, path);
        if (Files.notExists(saveDir)) {
            Files.createDirectories(saveDir);
        }

        var filename =
                idGenerator.objectId() + meta.getName().substring(meta.getName().lastIndexOf('.'));
        Files.write(saveDir.resolve(filename), content);

        return Paths.get(path, filename).toString();
    }
}
