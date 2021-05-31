package net.jaggerwang.scip.stat.usecase;

import java.time.LocalDateTime;
import net.jaggerwang.scip.common.entity.PostStatEntity;
import net.jaggerwang.scip.common.entity.UserStatEntity;
import net.jaggerwang.scip.stat.usecase.port.dao.PostStatDAO;
import net.jaggerwang.scip.stat.usecase.port.dao.UserStatDAO;

public class StatUsecase {
    private UserStatDAO userStatDAO;

    private PostStatDAO postStatDAO;

    public StatUsecase(UserStatDAO userStatDAO,
                       PostStatDAO postStatDAO) {
        this.userStatDAO = userStatDAO;
        this.postStatDAO = postStatDAO;
    }

    public UserStatEntity userStatInfoByUserId(Long userId) {
        var userStatEntity = userStatDAO.findByUserId(userId);
        return userStatEntity.orElseGet(() -> UserStatEntity.builder()
                .id(0L)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build());

    }

    public PostStatEntity postStatInfoByPostId(Long postId) {
        var postStatEntity = postStatDAO.findByPostId(postId);
        return postStatEntity.orElseGet(() -> PostStatEntity.builder()
                .id(0L)
                .postId(postId)
                .createdAt(LocalDateTime.now())
                .build());

    }
}
