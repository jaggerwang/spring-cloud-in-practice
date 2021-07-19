package net.jaggerwang.scip.gateway.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Jagger Wang
 */
@Component
@ReactiveFeignClient(value = "spring-cloud-in-practice-auth", path = "/user",
        configuration = ApiConfiguration.class)
public interface AuthUserService {
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    Mono<ApiResult<UserDTO>> info(@RequestParam Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByUsername")
    Mono<ApiResult<UserDTO>> infoByUsername(@RequestParam String username);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByMobile")
    Mono<ApiResult<UserDTO>> infoByMobile(@RequestParam String mobile);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByEmail")
    Mono<ApiResult<UserDTO>> infoByEmail(@RequestParam String email);

    @RequestMapping(method = RequestMethod.GET, value = "/rolesOfUser")
    Mono<ApiResult<List<RoleDTO>>> rolesOfUser(@RequestParam Long userId);
}
