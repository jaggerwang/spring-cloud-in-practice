package net.jaggerwang.scip.common.api.filter;

import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class CustomHttpTraceFilter extends HttpTraceFilter {
    public CustomHttpTraceFilter(HttpTraceRepository repository, HttpExchangeTracer tracer) {
        super(repository, tracer);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/actuator");
    }
}
