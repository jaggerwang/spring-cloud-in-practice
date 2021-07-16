package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
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
@FeignClient(value = "spring-cloud-in-practice-auth", path = "/auth",
        configuration = ApiConfiguration.class)
public interface AuthService {
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    ApiResult<UserDTO> info(Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByUsername")
    ApiResult<UserDTO> infoByUsername(String username);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByMobile")
    ApiResult<UserDTO> infoByMobile(String mobile);

    @RequestMapping(method = RequestMethod.GET, value = "/infoByEmail")
    ApiResult<UserDTO> infoByEmail(String email);

    @RequestMapping(method = RequestMethod.GET, value = "/rolesOfUser")
    ApiResult<List<RoleDTO>> rolesOfUser(Long userId);
}
