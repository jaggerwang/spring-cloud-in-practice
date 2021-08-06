package net.jaggerwang.scip.file.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.adapter.api.controller.BaseController;
import net.jaggerwang.scip.common.entity.FileBO;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;
import net.jaggerwang.scip.common.usecase.exception.*;
import net.jaggerwang.scip.file.usecase.FileUsecase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
    @Value("${file.base-url}")
    protected String baseUrl;

    protected FileUsecase fileUsecase;

    public FileController(HttpServletRequest request, ObjectMapper objectMapper,
                          FileUsecase fileUsecase) {
        super(request, objectMapper);

        this.fileUsecase = fileUsecase;
    }

    protected FileDTO fullFileDTO(FileBO fileBO) {
        var fileDTO = FileDTO.fromBO(fileBO);

        var url = "";
        if (fileDTO.getRegion() == FileBO.Region.LOCAL) {
            url = baseUrl + Paths.get("/", fileDTO.getBucket(), fileDTO.getPath()).toString();
        }
        fileDTO.setUrl(url);

        if (fileDTO.getMeta().getType().startsWith("image/")) {
            var thumbs = new HashMap<FileBO.ThumbType, String>(8);
            thumbs.put(FileBO.ThumbType.SMALL,
                    String.format("%s?process=%s", url, "thumb-small"));
            thumbs.put(FileBO.ThumbType.MIDDLE,
                    String.format("%s?process=%s", url, "thumb-middle"));
            thumbs.put(FileBO.ThumbType.LARGE,
                    String.format("%s?process=%s", url, "thumb-large"));
            thumbs.put(FileBO.ThumbType.HUGE,
                    String.format("%s?process=%s", url, "thumb-huge"));
            fileDTO.setThumbs(thumbs);
        }

        return fileDTO;
    }

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

        return new ApiResult(fileBOs.stream().map(this::fullFileDTO).collect(Collectors.toList()));
    }

    @GetMapping("/info")
    public ApiResult<FileDTO> info(@RequestParam Long id) {
        var fileBO = fileUsecase.info(id);
        if (fileBO.isEmpty()) {
            throw new NotFoundException("文件未找到");
        }

        return new ApiResult(fullFileDTO(fileBO.get()));
    }

    @GetMapping("/infos")
    public ApiResult<List<FileDTO>> infos(@RequestParam String ids,
                                          @RequestParam(defaultValue = "true") Boolean keepNull) {
        var idList = Arrays.stream(ids.split(",")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        var fileBOs = fileUsecase.infos(idList, keepNull);

        return new ApiResult(fileBOs.stream().map(this::fullFileDTO).collect(Collectors.toList()));
    }
}
