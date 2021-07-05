package net.jaggerwang.scip.auth.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.auth.usecase.AuthUsecase;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AuthUsecase authUsecase;
}
