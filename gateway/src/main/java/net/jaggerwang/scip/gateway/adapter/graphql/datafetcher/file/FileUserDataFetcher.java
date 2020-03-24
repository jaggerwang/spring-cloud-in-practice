package net.jaggerwang.scip.gateway.adapter.graphql.datafetcher.file;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import org.springframework.stereotype.Component;

@Component
public class FileUserDataFetcher extends AbstractFileDataFetcher implements DataFetcher {
    @Override
    public Object get(DataFetchingEnvironment env) {
        FileDto fileDto = env.getSource();
        return userAsyncService.info(fileDto.getUserId());
    }
}
