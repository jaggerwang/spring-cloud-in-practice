package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.HashMap;

@Component
public class FileDataFetchers extends AbstractDataFetchers {
    @Value("${service.file.base-url}")
    private String baseUrl;

    public DataFetcher user() {
        return env -> {
            FileDto fileDto = env.getSource();
            return userAsyncService.info(fileDto.getUserId());
        };
    }

    public DataFetcher url() {
        return env -> {
            FileDto fileDto = env.getSource();
            return generateUrl(fileDto);
        };
    }

    private String generateUrl(FileDto fileDto) {
        if ("LOCAL".equals(fileDto.getRegion())) {
            return baseUrl
                    + Paths.get("/", fileDto.getBucket(), fileDto.getPath()).toString();
        } else {
            return "";
        }
    }

    public DataFetcher thumbs() {
        return env -> {
            FileDto fileDto = env.getSource();
            var type = fileDto.getMeta().getType();
            if (type.startsWith("image/")) {
                var thumbs = new HashMap<String, String>();
                var u = generateUrl(fileDto);
                thumbs.put("SMALL",
                        String.format("%s?process=%s", u, "thumb-small"));
                thumbs.put("MIDDLE",
                        String.format("%s?process=%s", u, "thumb-middle"));
                thumbs.put("LARGE",
                        String.format("%s?process=%s", u, "thumb-large"));
                thumbs.put("HUGE", String.format("%s?process=%s", u, "thumb-huge"));
                return thumbs;
            } else {
                return null;
            }
        };
    }
}
