package net.jaggerwang.scip.file.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.common.usecase.exception.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.jaggerwang.scip.file.adapter.controller.dto.FileDto;
import net.jaggerwang.scip.file.entity.FileEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
public class FileController extends AbstractController {
    @PostMapping("/upload")
    public RootDto upload(@RequestParam(defaultValue = "LOCAL") FileEntity.Region region,
                          @RequestParam(defaultValue = "") String bucket,
                          @RequestParam(defaultValue = "") String path,
                          @RequestParam("file") List<MultipartFile> files) throws IOException {
        var fileEntities = new ArrayList<FileEntity>();
        for (var file : files) {
            var content = file.getBytes();

            var meta = FileEntity.Meta.builder()
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .type(file.getContentType())
                    .build();
            var fileEntity = FileEntity.builder()
                    .userId(loggedUserId())
                    .region(region)
                    .bucket(bucket)
                    .meta(meta).build();

            fileEntity = fileUsecase.upload(path, content, fileEntity);

            fileEntities.add(fileEntity);
        }

        return new RootDto().addDataEntry("files", fileEntities.stream()
                .map(FileDto::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/info")
    public RootDto info(@RequestParam Long id) {
        var fileEntity = fileUsecase.info(id);
        if (fileEntity.isEmpty()) {
            throw new NotFoundException("文件未找到");
        }

        return new RootDto().addDataEntry("file", FileDto.fromEntity(fileEntity.get()));
    }

    @GetMapping("/infos")
    public RootDto infos(@RequestParam String ids) {
        var idList = Arrays.stream(ids.split(",")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        var fileEntities = fileUsecase.infos(idList, true);

        return new RootDto().addDataEntry("file", fileEntities.stream()
                .map(FileDto::fromEntity).collect(Collectors.toList()));
    }
}
