package net.jaggerwang.scip.file.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.entity.FileBO;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import net.jaggerwang.scip.file.usecase.FileUsecase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Paths;
import java.util.HashMap;

abstract public class AbstractController {
    @Value("${file.base-url}")
    protected String baseUrl;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected FileUsecase fileUsecase;

    @Autowired
    protected HttpServletRequest request;

    protected Long loggedUserId() {
        var xUserId = request.getHeader("X-User-Id");
        if (StringUtils.isEmpty(xUserId)) return null;
        return Long.parseLong(xUserId);
    }

    protected FileDTO fullFileDto(FileBO fileBO) {
        var fileDto = FileDTO.fromBO(fileBO);

        var url = "";
        if (fileDto.getRegion() == FileBO.Region.LOCAL) {
            url = baseUrl + Paths.get("/", fileDto.getBucket(), fileDto.getPath()).toString();
        }
        fileDto.setUrl(url);

        if (fileDto.getMeta().getType().startsWith("image/")) {
            var thumbs = new HashMap<FileBO.ThumbType, String>(8);
            thumbs.put(FileBO.ThumbType.SMALL,
                    String.format("%s?process=%s", url, "thumb-small"));
            thumbs.put(FileBO.ThumbType.MIDDLE,
                    String.format("%s?process=%s", url, "thumb-middle"));
            thumbs.put(FileBO.ThumbType.LARGE,
                    String.format("%s?process=%s", url, "thumb-large"));
            thumbs.put(FileBO.ThumbType.HUGE,
                    String.format("%s?process=%s", url, "thumb-huge"));
            fileDto.setThumbs(thumbs);
        }

        return fileDto;
    }
}
