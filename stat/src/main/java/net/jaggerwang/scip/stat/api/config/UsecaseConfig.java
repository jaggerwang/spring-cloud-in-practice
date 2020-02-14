package net.jaggerwang.scip.stat.api.config;

import net.jaggerwang.scip.stat.usecase.StatUsecase;
import net.jaggerwang.scip.stat.usecase.port.repository.PostStatRepository;
import net.jaggerwang.scip.stat.usecase.port.repository.UserStatRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UsecaseConfig {
    @Bean
    public StatUsecase statUsecase(UserStatRepository userStatRepository,
                                   PostStatRepository postStatRepository) {
        return new StatUsecase(userStatRepository, postStatRepository);
    }
}
