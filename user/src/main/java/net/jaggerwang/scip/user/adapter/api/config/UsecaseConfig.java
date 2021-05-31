package net.jaggerwang.scip.user.adapter.api.config;

import net.jaggerwang.scip.user.usecase.UserUsecase;
import net.jaggerwang.scip.user.usecase.port.dao.RoleDAO;
import net.jaggerwang.scip.user.usecase.port.dao.UserDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UsecaseConfig {
    @Bean
    public UserUsecase userUsecase(UserDAO userDAO, RoleDAO roleDAO) {
        return new UserUsecase(userDAO, roleDAO);
    }
}
