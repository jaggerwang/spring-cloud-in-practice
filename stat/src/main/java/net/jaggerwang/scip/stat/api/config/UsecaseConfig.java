package net.jaggerwang.scip.stat.api.config;

import net.jaggerwang.scip.stat.usecase.StatUsecases;
import net.jaggerwang.scip.stat.usecase.port.repository.PostStatRepository;
import net.jaggerwang.scip.stat.usecase.port.repository.UserStatRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsecaseConfig {
    @Bean
    public StatUsecases statUsecases(UserStatRepository userStatRepository,
            PostStatRepository postStatRepository) {
        return new StatUsecases(userStatRepository, postStatRepository);
    }
}
