package net.jaggerwang.scip.stat.usecase.port.repository;

import java.util.Optional;

import net.jaggerwang.scip.stat.entity.PostStatEntity;

public interface PostStatRepository {
    PostStatEntity save(PostStatEntity postStatEntity);

    Optional<PostStatEntity> findById(Long id);

    Optional<PostStatEntity> findByPostId(Long postId);
}
