package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDTO;

public interface StatSyncService {
    UserStatDTO ofUser(Long userId);

    PostStatDTO ofPost(Long postId);
}
