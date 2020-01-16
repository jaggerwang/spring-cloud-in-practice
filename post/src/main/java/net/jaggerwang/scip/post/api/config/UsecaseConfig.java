package net.jaggerwang.scip.post.api.config;

import net.jaggerwang.scip.post.usecase.PostUsecases;
import net.jaggerwang.scip.post.usecase.port.repository.PostRepository;
import net.jaggerwang.scip.post.usecase.port.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsecaseConfig {
    @Bean
    public PostUsecases postUsecases(PostRepository postRepository, UserService userService) {
        return new PostUsecases(postRepository, userService);
    }
}
