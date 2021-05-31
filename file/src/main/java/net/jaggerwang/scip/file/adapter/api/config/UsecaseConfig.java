package net.jaggerwang.scip.file.adapter.api.config;

import net.jaggerwang.scip.file.usecase.FileUsecase;
import net.jaggerwang.scip.file.usecase.port.dao.FileDAO;
import net.jaggerwang.scip.file.usecase.port.service.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UsecaseConfig {
    @Bean
    public FileUsecase fileUsecase(FileDAO fileDAO, StorageService storageService) {
        return new FileUsecase(fileDAO, storageService);
    }
}
