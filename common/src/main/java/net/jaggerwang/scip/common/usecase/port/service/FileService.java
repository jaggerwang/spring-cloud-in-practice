package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Jagger Wang
 */
@Component
@FeignClient(value = "spring-cloud-in-practice-file", configuration = ApiConfiguration.class)
public interface FileService {
    @RequestMapping(method = RequestMethod.GET, value = "/file/info")
    ApiResult<FileDTO> fileInfo(@RequestParam Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/file/infos")
    ApiResult<List<FileDTO>> fileInfos(@RequestParam List<Long> ids,
                                       @RequestParam Boolean keepNull);
}
