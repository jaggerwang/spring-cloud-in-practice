package net.jaggerwang.scip.stat.adapter.api.config;

import net.jaggerwang.scip.stat.usecase.StatUsecase;
import net.jaggerwang.scip.stat.usecase.port.dao.PostStatDAO;
import net.jaggerwang.scip.stat.usecase.port.dao.UserStatDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UsecaseConfig {
    @Bean
    public StatUsecase statUsecase(UserStatDAO userStatDAO,
                                   PostStatDAO postStatDAO) {
        return new StatUsecase(userStatDAO, postStatDAO);
    }
}
