package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
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
@FeignClient(value = "spring-cloud-in-practice-user", path = "/follow",
        configuration = ApiConfiguration.class)
public interface UserFollowService {
    @RequestMapping(method = RequestMethod.GET, value = "/following")
    ApiResult<List<Long>> following(@RequestParam Long userId, @RequestParam Long limit,
                                       @RequestParam Long offset);

    @RequestMapping(method = RequestMethod.GET, value = "/followingCount")
    ApiResult<Long> followingCount(@RequestParam Long userId);
}
