package net.jaggerwang.scip.file.adapter.api.controller;

import net.jaggerwang.scip.common.entity.FileBO;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;
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

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/file")
public class FileController extends AbstractController {
    @PostMapping("/upload")
    public ApiResult<List<FileDTO>> upload(@RequestParam(defaultValue = "LOCAL") FileBO.Region region,
                                           @RequestParam(defaultValue = "") String bucket,
                                           @RequestParam(defaultValue = "") String path,
                                           @RequestParam("file") List<MultipartFile> files) throws IOException {
        var fileBOs = new ArrayList<FileBO>();
        for (var file : files) {
            var content = file.getBytes();

            var meta = FileBO.Meta.builder()
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .type(file.getContentType())
                    .build();
            var fileBO = FileBO.builder()
                    .userId(loggedUserId())
                    .region(region)
                    .bucket(bucket)
                    .meta(meta).build();

            fileBO = fileUsecase.upload(path, content, fileBO);

            fileBOs.add(fileBO);
        }

        return new ApiResult(fileBOs.stream().map(this::fullFileDto).collect(Collectors.toList()));
    }

    @GetMapping("/info")
    public ApiResult<FileDTO> info(@RequestParam Long id) {
        var fileBO = fileUsecase.info(id);
        if (fileBO.isEmpty()) {
            throw new NotFoundException("文件未找到");
        }

        return new ApiResult(fullFileDto(fileBO.get()));
    }

    @GetMapping("/infos")
    public ApiResult<List<FileDTO>> infos(@RequestParam String ids,
                                          @RequestParam(defaultValue = "true") Boolean keepNull) {
        var idList = Arrays.stream(ids.split(",")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        var fileBOs = fileUsecase.infos(idList, keepNull);

        return new ApiResult(fileBOs.stream().map(this::fullFileDto).collect(Collectors.toList()));
    }
}
