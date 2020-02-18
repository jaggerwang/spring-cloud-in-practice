package net.jaggerwang.scip.common.usecase.port.service.sync;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostDto;

public interface PostSyncService {
    PostDto info(Long id);
}
