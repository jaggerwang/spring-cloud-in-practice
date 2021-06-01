package net.jaggerwang.scip.file.adapter.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.jaggerwang.scip.common.entity.FileBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.file.adapter.dao.jpa.FileRepository;
import net.jaggerwang.scip.file.adapter.dao.jpa.entity.File;
import net.jaggerwang.scip.file.usecase.port.dao.FileDAO;

@Component
public class FileDAOImpl implements FileDAO {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public FileBO save(FileBO fileBO) {
        return fileRepository.save(File.fromEntity(fileBO)).toEntity();
    }

    @Override
    public Optional<FileBO> findById(Long id) {
        return fileRepository.findById(id).map(file -> file.toEntity());
    }

    @Override
    public List<FileBO> findAllById(List<Long> ids) {
        var fileDos = fileRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(file -> file.getId(), file -> file.toEntity()));

        var fileEntities = new FileBO[ids.size()];
        IntStream.range(0, ids.size()).forEach(i -> {
            var id = ids.get(i);
            if (fileDos.containsKey(id)) {
                fileEntities[i] = fileDos.get(id);
            }
        });

        return Arrays.asList(fileEntities);
    }
}
