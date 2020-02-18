package net.jaggerwang.scip.common.adapter.service.sync;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDto;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class FileSyncService extends InternalSyncService implements net.jaggerwang.scip.common.usecase.port.service.sync.FileSyncService {
    public FileSyncService(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                           ObjectMapper objectMapper) {
        super(restTemplate, cbFactory, objectMapper);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public FileDto info(Long id) {
        var params = new HashMap<String, String>();
        params.put("id", id.toString());
        var response = getData("/file/info", params);
        return objectMapper.convertValue(response.get("file"), FileDto.class);
    }

    @Override
    public List<FileDto> infos(List<Long> ids, @Nullable Boolean keepNull) {
        var params = new HashMap<String, String>();
        params.put("ids", ids.toString());
        if (keepNull != null) {
            params.put("keepNull", keepNull.toString());
        }
        var response = getData("/file/infos", params);
        return objectMapper.convertValue(response.get("files"), new TypeReference<>() {});
    }
}
