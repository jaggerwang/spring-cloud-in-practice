package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.file;

import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.AbstractDataFetcher;
import net.jaggerwang.scip.common.entity.FileEntity;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Paths;

abstract  public class AbstractFileDataFetcher extends AbstractDataFetcher {
    @Value("${service.file.base-url}")
    private String baseUrl;

    protected String generateUrl(FileDto fileDto) {
        if (FileEntity.Region.LOCAL == fileDto.getRegion()) {
            return baseUrl
                    + Paths.get("/", fileDto.getBucket(), fileDto.getPath()).toString();
        } else {
            return "";
        }
    }
}
