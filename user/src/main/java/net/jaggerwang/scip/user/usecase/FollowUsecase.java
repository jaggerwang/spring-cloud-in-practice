package net.jaggerwang.scip.user.usecase;

import java.util.List;

import net.jaggerwang.scip.user.usecase.port.dao.FollowDAO;
import org.springframework.stereotype.Component;

/**
 * @author Jagger Wang
 */
@Component
public class FollowUsecase {
    private FollowDAO followDAO;

    public FollowUsecase(FollowDAO followDAO) {
        this.followDAO = followDAO;
    }

    public void follow(Long followerId, Long followingId) {
        followDAO.follow(followerId, followingId);
    }

    public void unfollow(Long followerId, Long followingId) {
        followDAO.unfollow(followerId, followingId);
    }

    public Boolean isFollowing(Long followerId, Long followingId) {
        return followDAO.isFollowing(followerId, followingId);
    }

    public List<Long> following(Long followerId, Long limit, Long offset) {
        return followDAO.following(followerId, limit, offset);
    }

    public Long followingCount(Long followerId) {
        return followDAO.followingCount(followerId);
    }

    public List<Long> follower(Long followingId, Long limit, Long offset) {
        return followDAO.follower(followingId, limit, offset);
    }

    public Long followerCount(Long followingId) {
        return followDAO.followerCount(followingId);
    }
}
