package net.jaggerwang.scip.gateway.adapter.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileResolver extends AbstractResolver implements GraphQLResolver<FileDto> {
    @Value("${service.file.base-url}")
    private String baseUrl;

    public Mono<UserDto> user(FileDto fileDto) {
        return userService.info(fileDto.getUserId());
    }

    public String url(FileDto fileDto) {
        if ("LOCAL".equals(fileDto.getRegion())) {
            return baseUrl
                    + Paths.get("/", fileDto.getBucket(), fileDto.getPath()).toString();
        } else {
            return "";
        }
    }

    public Map<String, String> thumbs(FileDto fileDto) {
        var type = (String) fileDto.getMeta().get("type");
        if (type.startsWith("image/")) {
            var thumbs = new HashMap<String, String>();
            var u = url(fileDto);
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
    }
}
