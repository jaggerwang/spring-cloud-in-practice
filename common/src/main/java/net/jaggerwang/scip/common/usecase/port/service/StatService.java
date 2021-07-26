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
@FeignClient(value = "spring-cloud-in-practice-stat", configuration = ApiConfiguration.class)
public interface StatService {
    @RequestMapping(method = RequestMethod.GET, value = "/stat/ofUser")
    ApiResult<UserStatDTO> statOfUser(@RequestParam Long userId);

    @RequestMapping(method = RequestMethod.GET, value = "/stat/ofPost")
    ApiResult<PostStatDTO> statOfPost(@RequestParam Long postId);
}
