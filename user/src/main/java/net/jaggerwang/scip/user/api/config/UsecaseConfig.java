package net.jaggerwang.scip.user.api.config;

import net.jaggerwang.scip.user.usecase.UserUsecases;
import net.jaggerwang.scip.user.usecase.port.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsecaseConfig {
    @Bean
    public UserUsecases userUsecases(UserRepository userRepository) {
        return new UserUsecases(userRepository);
    }
}
