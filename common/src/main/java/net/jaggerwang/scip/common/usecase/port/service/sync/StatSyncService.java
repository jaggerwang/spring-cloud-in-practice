package net.jaggerwang.scip.common.usecase.port.service.sync;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDto;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDto;

public interface StatSyncService {
    UserStatDto ofUser(Long userId);

    PostStatDto ofPost(Long postId);
}
