package net.jaggerwang.scip.user.api.config;

import net.jaggerwang.scip.user.usecase.UserUsecase;
import net.jaggerwang.scip.user.usecase.port.repository.RoleRepository;
import net.jaggerwang.scip.user.usecase.port.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UsecaseConfig {
    @Bean
    public UserUsecase userUsecase(UserRepository userRepository, RoleRepository roleRepository) {
        return new UserUsecase(userRepository, roleRepository);
    }
}
