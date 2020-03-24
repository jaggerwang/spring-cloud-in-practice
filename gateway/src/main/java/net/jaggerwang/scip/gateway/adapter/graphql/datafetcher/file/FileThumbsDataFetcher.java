package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.file;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class FileThumbsDataFetcher extends AbstractFileDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
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
    }
}
