package net.jaggerwang.scip.file.adapter.api.controller;

import net.jaggerwang.scip.common.entity.FileBO;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import net.jaggerwang.scip.common.usecase.exception.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
public class FileController extends AbstractController {
    @PostMapping("/upload")
    public RootDTO upload(@RequestParam(defaultValue = "LOCAL") FileBO.Region region,
                          @RequestParam(defaultValue = "") String bucket,
                          @RequestParam(defaultValue = "") String path,
                          @RequestParam("file") List<MultipartFile> files) throws IOException {
        var fileEntities = new ArrayList<FileBO>();
        for (var file : files) {
            var content = file.getBytes();

            var meta = FileBO.Meta.builder()
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .type(file.getContentType())
                    .build();
            var fileEntity = FileBO.builder()
                    .userId(loggedUserId())
                    .region(region)
                    .bucket(bucket)
                    .meta(meta).build();

            fileEntity = fileUsecase.upload(path, content, fileEntity);

            fileEntities.add(fileEntity);
        }

        return new RootDTO().addDataEntry("files", fileEntities.stream()
                .map(FileDTO::fromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/info")
    public RootDTO info(@RequestParam Long id) {
        var fileEntity = fileUsecase.info(id);
        if (fileEntity.isEmpty()) {
            throw new NotFoundException("文件未找到");
        }

        return new RootDTO().addDataEntry("file", FileDTO.fromEntity(fileEntity.get()));
    }

    @GetMapping("/infos")
    public RootDTO infos(@RequestParam String ids,
                         @RequestParam(defaultValue = "true") Boolean keepNull) {
        var idList = Arrays.stream(ids.split(",")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        var fileEntities = fileUsecase.infos(idList, keepNull);

        return new RootDTO().addDataEntry("files", fileEntities.stream()
                .map(FileDTO::fromEntity).collect(Collectors.toList()));
    }
}
