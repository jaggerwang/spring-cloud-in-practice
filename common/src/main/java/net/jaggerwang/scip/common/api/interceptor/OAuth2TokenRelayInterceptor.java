package net.jaggerwang.scip.common.api.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

public class OAuth2TokenRelayInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var requestAttrs = RequestContextHolder.getRequestAttributes();
        if (requestAttrs instanceof ServletRequestAttributes) {
            var servletRequestAttrs = (ServletRequestAttributes) requestAttrs;
            var authHeader = servletRequestAttrs.getRequest().getHeader("Authorization");
            if (authHeader != null) {
                request.getHeaders().add("Authorization", authHeader);
            }
        }
        return execution.execute(request, body);
    }
}
