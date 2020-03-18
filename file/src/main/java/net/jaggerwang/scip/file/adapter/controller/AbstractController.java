package net.jaggerwang.scip.file.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.jaggerwang.scip.file.usecase.FileUsecase;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

abstract public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected FileUsecase fileUsecase;

    @Autowired
    protected HttpServletRequest request;

    protected Long loggedUserId() {
        var xUserId = request.getHeader("X-User-Id");
        if (StringUtils.isEmpty(xUserId)) return null;
        return Long.parseLong(xUserId);
    }
}
