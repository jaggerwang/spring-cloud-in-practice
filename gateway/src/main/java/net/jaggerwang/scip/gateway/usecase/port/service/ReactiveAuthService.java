package net.jaggerwang.scip.gateway.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Jagger Wang
 */
@Component
@ReactiveFeignClient(value = "spring-cloud-in-practice-auth", path = "/auth",
        configuration = ApiConfiguration.class, qualifier = "reactiveAuthService")
public interface ReactiveAuthService {
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    Mono<ApiResult<UserDTO>> info(Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByUsername")
    Mono<ApiResult<UserDTO>> infoByUsername(String username);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByMobile")
    Mono<ApiResult<UserDTO>> infoByMobile(String mobile);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByEmail")
    Mono<ApiResult<UserDTO>> infoByEmail(String email);

    @RequestMapping(method = RequestMethod.GET, value = "/rolesOfUser")
    Mono<ApiResult<List<RoleDTO>>> rolesOfUser(Long userId);
}
