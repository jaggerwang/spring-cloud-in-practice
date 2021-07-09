package net.jaggerwang.scip.common.usecase.port.service;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostDTO;

/**
 * @author Jagger Wang
 */
public interface PostService {
    PostDTO info(Long id);
}
