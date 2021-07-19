package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
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
@FeignClient(value = "spring-cloud-in-practice-auth", path = "/user",
        configuration = ApiConfiguration.class)
public interface AuthUserService {
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    ApiResult<UserDTO> info(@RequestParam Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByUsername")
    ApiResult<UserDTO> infoByUsername(@RequestParam String username);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByMobile")
    ApiResult<UserDTO> infoByMobile(@RequestParam String mobile);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByEmail")
    ApiResult<UserDTO> infoByEmail(@RequestParam String email);

    @RequestMapping(method = RequestMethod.GET, value = "/rolesOfUser")
    ApiResult<List<RoleDTO>> rolesOfUser(@RequestParam Long userId);
}
