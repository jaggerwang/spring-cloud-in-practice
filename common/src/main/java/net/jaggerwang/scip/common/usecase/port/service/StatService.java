package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.adapter.service.feign.ApiConfiguration;
import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Jagger Wang
 */
@Component
@FeignClient(value = "spring-cloud-in-practice-stat", path = "/stat",
        configuration = ApiConfiguration.class)
public interface StatService {
    @RequestMapping(method = RequestMethod.GET, value = "/ofUser")
    UserStatDTO ofUser(Long userId);

    @RequestMapping(method = RequestMethod.GET, value = "/ofPost")
    PostStatDTO ofPost(Long postId);
}
