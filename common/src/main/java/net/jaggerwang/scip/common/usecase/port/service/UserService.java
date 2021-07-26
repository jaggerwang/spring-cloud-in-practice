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
@FeignClient(value = "spring-cloud-in-practice-user", configuration = ApiConfiguration.class)
public interface UserService {
    @RequestMapping(method = RequestMethod.GET, value = "/user/info")
    ApiResult<UserDTO> userInfo(@RequestParam Long id);

    @RequestMapping(method = RequestMethod.GET, value = "/user/infoByUsername")
    ApiResult<UserDTO> userInfoByUsername(@RequestParam String username);

    @RequestMapping(method = RequestMethod.GET, value = "/user/infoByMobile")
    ApiResult<UserDTO> userInfoByMobile(@RequestParam String mobile);

    @RequestMapping(method = RequestMethod.GET, value = "/user/infoByEmail")
    ApiResult<UserDTO> userInfoByEmail(@RequestParam String email);

    @RequestMapping(method = RequestMethod.GET, value = "/user/rolesOfUser")
    ApiResult<List<RoleDTO>> rolesOfUser(@RequestParam Long userId);

    @RequestMapping(method = RequestMethod.GET, value = "/follow/following")
    ApiResult<List<Long>> following(@RequestParam Long userId, @RequestParam Long limit,
                                    @RequestParam Long offset);

    @RequestMapping(method = RequestMethod.GET, value = "/follow/followingCount")
    ApiResult<Long> followingCount(@RequestParam Long userId);
}
