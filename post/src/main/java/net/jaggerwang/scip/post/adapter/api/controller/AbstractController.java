package net.jaggerwang.scip.post.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.jaggerwang.scip.post.usecase.PostUsecase;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jagger Wang
 */
abstract public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PostUsecase postUsecase;

    @Autowired
    protected HttpServletRequest request;

    protected Long loggedUserId() {
        var xUserId = request.getHeader("X-User-Id");
        if (StringUtils.isEmpty(xUserId)) return null;
        return Long.parseLong(xUserId);
    }
}
