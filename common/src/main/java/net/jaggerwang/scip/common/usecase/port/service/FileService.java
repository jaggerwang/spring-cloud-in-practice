package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.dto.FileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Jagger Wang
 */
@Component
@FeignClient(value = "spring-cloud-in-practice-file", path = "/file",
        configuration = ApiConfiguration.class)
public interface FileService {
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    FileDTO info(Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/infos")
    List<FileDTO> infos(List<Long> ids, Boolean keepNull);
}
