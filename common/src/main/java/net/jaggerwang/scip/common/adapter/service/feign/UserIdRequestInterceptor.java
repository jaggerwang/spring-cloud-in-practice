package net.jaggerwang.scip.common.adapter.service.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Jagger Wang
 */
public class UserIdRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            var request = ((ServletRequestAttributes) requestAttributes).getRequest();
            template.header("X-User-Id", request.getHeader("X-User-Id"));
        }
    }
}
