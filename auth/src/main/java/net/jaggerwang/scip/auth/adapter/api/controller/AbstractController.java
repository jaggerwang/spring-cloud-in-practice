package net.jaggerwang.scip.auth.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.auth.usecase.AuthUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jagger Wang
 */
abstract public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AuthUsecase authUsecase;

    @Autowired
    protected HttpServletRequest request;

    protected Long loggedUserId() {
        var xUserId = request.getHeader("X-User-Id");
        if (StringUtils.isEmpty(xUserId)) return null;
        return Long.parseLong(xUserId);
    }
}
