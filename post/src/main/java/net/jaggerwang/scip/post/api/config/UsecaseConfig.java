package net.jaggerwang.scip.post.api.config;

import net.jaggerwang.scip.common.usecase.port.service.sync.UserSyncService;
import net.jaggerwang.scip.post.usecase.PostUsecase;
import net.jaggerwang.scip.post.usecase.port.repository.PostRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UsecaseConfig {
    @Bean
    public PostUsecase postUsecase(PostRepository postRepository, UserSyncService userSyncService) {
        return new PostUsecase(postRepository, userSyncService);
    }
}
