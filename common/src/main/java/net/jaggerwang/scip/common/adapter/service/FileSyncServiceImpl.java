package net.jaggerwang.scip.common.adapter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;
import net.jaggerwang.scip.common.usecase.port.service.FileSyncService;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class FileSyncServiceImpl extends InternalSyncService implements FileSyncService {
    public FileSyncServiceImpl(RestTemplate restTemplate, CircuitBreakerFactory cbFactory,
                               ObjectMapper objectMapper) {
        super(restTemplate, cbFactory, objectMapper);
    }

    @Override
    public Optional<String> getCircuitBreakerName(URI uri) {
        return Optional.of("normal");
    }

    @Override
    public FileDTO info(Long id) {
        var params = new HashMap<String, String>();
        params.put("id", id.toString());
        var response = getData("/file/info", params);
        return objectMapper.convertValue(response.get("file"), FileDTO.class);
    }

    @Override
    public List<FileDTO> infos(List<Long> ids, @Nullable Boolean keepNull) {
        var params = new HashMap<String, String>();
        params.put("ids", ids.toString());
        if (keepNull != null) {
            params.put("keepNull", keepNull.toString());
        }
        var response = getData("/file/infos", params);
        return objectMapper.convertValue(response.get("files"), new TypeReference<>() {});
    }
}
