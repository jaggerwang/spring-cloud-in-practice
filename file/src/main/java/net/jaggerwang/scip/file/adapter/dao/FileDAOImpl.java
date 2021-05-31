package net.jaggerwang.scip.file.adapter.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.file.adapter.dao.jpa.FileRepository;
import net.jaggerwang.scip.file.adapter.dao.jpa.entity.File;
import net.jaggerwang.scip.common.entity.FileEntity;
import net.jaggerwang.scip.file.usecase.port.dao.FileDAO;

@Component
public class FileDAOImpl implements FileDAO {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public FileEntity save(FileEntity fileEntity) {
        return fileRepository.save(File.fromEntity(fileEntity)).toEntity();
    }

    @Override
    public Optional<FileEntity> findById(Long id) {
        return fileRepository.findById(id).map(file -> file.toEntity());
    }

    @Override
    public List<FileEntity> findAllById(List<Long> ids) {
        var fileDos = fileRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(file -> file.getId(), file -> file.toEntity()));

        var fileEntities = new FileEntity[ids.size()];
        IntStream.range(0, ids.size()).forEach(i -> {
            var id = ids.get(i);
            if (fileDos.containsKey(id)) {
                fileEntities[i] = fileDos.get(id);
            }
        });

        return Arrays.asList(fileEntities);
    }
}
