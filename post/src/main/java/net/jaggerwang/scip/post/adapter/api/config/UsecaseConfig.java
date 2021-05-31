package net.jaggerwang.scip.post.adapter.api.config;

import net.jaggerwang.scip.common.usecase.port.service.UserSyncService;
import net.jaggerwang.scip.post.usecase.PostUsecase;
import net.jaggerwang.scip.post.usecase.port.dao.PostDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UsecaseConfig {
    @Bean
    public PostUsecase postUsecase(PostDAO postDAO, UserSyncService userSyncService) {
        return new PostUsecase(postDAO, userSyncService);
    }
}
