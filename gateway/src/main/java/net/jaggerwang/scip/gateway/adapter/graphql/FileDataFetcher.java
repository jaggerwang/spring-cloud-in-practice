package net.jaggerwang.scip.gateway.adapter.graphql;

import graphql.schema.DataFetcher;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.LinkedHashMap;

@Component
public class FileDataFetcher extends AbstractDataFetcher {
    @Value("${service.file.base-url}")
    private String baseUrl;

    public DataFetcher user() {
        return env -> {
            FileDto fileDto = env.getSource();
            return monoWithContext(userAsyncService.info(fileDto.getUserId()), env);
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

    public DataFetcher url() {
        return env -> {
            FileDto fileDto = env.getSource();
            return generateUrl(fileDto);
        };
    }

    public DataFetcher thumbs() {
        return env -> {
            FileDto fileDto = env.getSource();
            var type = fileDto.getMeta().getType();
            if (type.startsWith("image/")) {
                var thumbs = new LinkedHashMap<String, String>();
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
