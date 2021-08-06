package net.jaggerwang.scip.gateway.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.user.UserBindRequestDTO;
import net.jaggerwang.scip.gateway.adapter.service.feign.ApiConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
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
@ReactiveFeignClient(value = "spring-cloud-in-practice-user",
        configuration = ApiConfiguration.class)
public interface UserService {
    @RequestMapping(method = RequestMethod.POST, value = "/user/bind")
    Mono<ApiResult<UserDTO>> bind(@RequestBody UserBindRequestDTO requestDTO);

    @RequestMapping(method = RequestMethod.GET, value = "/user/info")
    Mono<ApiResult<UserDTO>> userInfo(@RequestParam Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/user/infoByUsername")
    Mono<ApiResult<UserDTO>> userInfoByUsername(@RequestParam String username);

    @RequestMapping(method = RequestMethod.GET, value = "/user/infoByMobile")
    Mono<ApiResult<UserDTO>> userInfoByMobile(@RequestParam String mobile);

    @RequestMapping(method = RequestMethod.GET, value = "/user/infoByEmail")
    Mono<ApiResult<UserDTO>> userInfoByEmail(@RequestParam String email);

    @RequestMapping(method = RequestMethod.GET, value = "/user/rolesOfUser")
    Mono<ApiResult<List<RoleDTO>>> rolesOfUser(@RequestParam Long userId);
}
