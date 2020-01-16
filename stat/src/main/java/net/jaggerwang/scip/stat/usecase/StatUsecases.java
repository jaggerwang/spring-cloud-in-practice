package net.jaggerwang.scip.stat.usecase;

import java.time.LocalDateTime;
import net.jaggerwang.scip.stat.entity.PostStatEntity;
import net.jaggerwang.scip.stat.entity.UserStatEntity;
import net.jaggerwang.scip.stat.usecase.port.repository.PostStatRepository;
import net.jaggerwang.scip.stat.usecase.port.repository.UserStatRepository;

public class StatUsecases {
    private UserStatRepository userStatRepository;

    private PostStatRepository postStatRepository;

    public StatUsecases(UserStatRepository userStatRepository,
            PostStatRepository postStatRepository) {
        this.userStatRepository = userStatRepository;
        this.postStatRepository = postStatRepository;
    }

    public UserStatEntity userStatInfoByUserId(Long userId) {
        var userStatEntity = userStatRepository.findByUserId(userId);
        return userStatEntity.orElseGet(() -> UserStatEntity.builder().id(0L).userId(userId).createdAt(LocalDateTime.now())
                .build());

    }

    public PostStatEntity postStatInfoByPostId(Long postId) {
        var postStatEntity = postStatRepository.findByPostId(postId);
        return postStatEntity.orElseGet(() -> PostStatEntity.builder().id(0L).postId(postId).createdAt(LocalDateTime.now())
                .build());

    }
}
