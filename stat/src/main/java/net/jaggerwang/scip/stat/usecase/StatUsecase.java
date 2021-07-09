package net.jaggerwang.scip.stat.usecase;

import java.time.LocalDateTime;
import net.jaggerwang.scip.common.entity.PostStatBO;
import net.jaggerwang.scip.common.entity.UserStatBO;
import net.jaggerwang.scip.stat.usecase.port.dao.PostStatDAO;
import net.jaggerwang.scip.stat.usecase.port.dao.UserStatDAO;
import org.springframework.stereotype.Component;

@Component
public class StatUsecase {
    private UserStatDAO userStatDAO;

    private PostStatDAO postStatDAO;

    public StatUsecase(UserStatDAO userStatDAO,
                       PostStatDAO postStatDAO) {
        this.userStatDAO = userStatDAO;
        this.postStatDAO = postStatDAO;
    }

    public UserStatBO userStatInfoByUserId(Long userId) {
        var userStatBO = userStatDAO.findByUserId(userId);
        return userStatBO.orElseGet(() -> UserStatBO.builder()
                .id(0L)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build());

    }

    public PostStatBO postStatInfoByPostId(Long postId) {
        var postStatBO = postStatDAO.findByPostId(postId);
        return postStatBO.orElseGet(() -> PostStatBO.builder()
                .id(0L)
                .postId(postId)
                .createdAt(LocalDateTime.now())
                .build());

    }
}
