package net.jaggerwang.scip.file.api.config;

import net.jaggerwang.scip.file.usecase.FileUsecase;
import net.jaggerwang.scip.file.usecase.port.repository.FileRepository;
import net.jaggerwang.scip.file.usecase.port.service.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class UsecaseConfig {
    @Bean
    public FileUsecase fileUsecase(FileRepository fileRepository, StorageService storageService) {
        return new FileUsecase(fileRepository, storageService);
    }
}
