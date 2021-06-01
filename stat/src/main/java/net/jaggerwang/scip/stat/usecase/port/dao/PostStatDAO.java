package net.jaggerwang.scip.stat.usecase.port.dao;

import java.util.Optional;

import net.jaggerwang.scip.common.entity.PostStatBO;

public interface PostStatDAO {
    PostStatBO save(PostStatBO postStatBO);

    Optional<PostStatBO> findById(Long id);

    Optional<PostStatBO> findByPostId(Long postId);
}
