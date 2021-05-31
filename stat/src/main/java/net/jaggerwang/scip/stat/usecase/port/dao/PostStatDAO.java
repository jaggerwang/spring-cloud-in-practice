package net.jaggerwang.scip.stat.usecase.port.dao;

import java.util.Optional;

import net.jaggerwang.scip.common.entity.PostStatEntity;

public interface PostStatDAO {
    PostStatEntity save(PostStatEntity postStatEntity);

    Optional<PostStatEntity> findById(Long id);

    Optional<PostStatEntity> findByPostId(Long postId);
}
