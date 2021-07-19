package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Jagger Wang
 */
@Component
@FeignClient(value = "spring-cloud-in-practice-stat", path = "/stat",
        configuration = ApiConfiguration.class)
public interface StatStatService {
    @RequestMapping(method = RequestMethod.GET, value = "/ofUser")
    ApiResult<UserStatDTO> ofUser(@RequestParam Long userId);

    @RequestMapping(method = RequestMethod.GET, value = "/ofPost")
    ApiResult<PostStatDTO> ofPost(@RequestParam Long postId);
}
