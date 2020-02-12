package net.jaggerwang.scip.file.api.config;

import net.jaggerwang.scip.file.usecase.FileUsecases;
import net.jaggerwang.scip.file.usecase.port.repository.FileRepository;
import net.jaggerwang.scip.file.usecase.port.service.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UsecaseConfig {
    @Bean
    public FileUsecases fileUsecases(FileRepository fileRepository, StorageService storageService) {
        return new FileUsecases(fileRepository, storageService);
    }
}
