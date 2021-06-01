package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostDTO;

public interface PostSyncService {
    PostDTO info(Long id);
}
