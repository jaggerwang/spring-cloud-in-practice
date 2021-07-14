package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Jagger Wang
 */
@Component
@FeignClient(value = "spring-cloud-in-practice-user", path = "/user",
        configuration = ApiConfiguration.class)
public interface UserService {
    @RequestMapping(method = RequestMethod.GET, value = "/following")
    List<UserDTO> following(Long userId, Long limit, Long offset);

    @RequestMapping(method = RequestMethod.GET, value = "/followingCount")
    Long followingCount(Long userId);
}
