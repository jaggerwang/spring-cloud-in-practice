package net.jaggerwang.scip.common.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jagger Wang
 */
abstract public class BaseController {
    protected HttpServletRequest request;

    protected ObjectMapper objectMapper;

    protected BaseController(HttpServletRequest request, ObjectMapper objectMapper) {
        this.request = request;
        this.objectMapper = objectMapper;
    }

    protected Long loggedUserId() {
        var xUserId = request.getHeader("X-User-Id");
        if (!StringUtils.hasText(xUserId)) {
            return null;
        }
        return Long.parseLong(xUserId);
    }
}
