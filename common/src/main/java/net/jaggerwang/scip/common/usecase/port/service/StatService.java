package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDTO;

/**
 * @author Jagger Wang
 */
public interface StatService {
    UserStatDTO ofUser(Long userId);

    PostStatDTO ofPost(Long postId);
}
