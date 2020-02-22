package net.jaggerwang.scip.common.api.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class HeadersRelayInterceptor implements ClientHttpRequestInterceptor {
    private Set<String> headers;

    public HeadersRelayInterceptor(Set<String> headers) {
        this.headers = headers;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        var requestAttrs = RequestContextHolder.getRequestAttributes();
        if (requestAttrs instanceof ServletRequestAttributes) {
            var upstreamRequest = ((ServletRequestAttributes) requestAttrs).getRequest();
            for (var header: headers) {
                request.getHeaders().addAll(header,
                        Collections.list(upstreamRequest.getHeaders(header)));
            }
        }
        return execution.execute(request, body);
    }
}
