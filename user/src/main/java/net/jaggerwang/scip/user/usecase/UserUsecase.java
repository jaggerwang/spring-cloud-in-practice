package net.jaggerwang.scip.user.usecase;

import java.util.List;

import net.jaggerwang.scip.user.usecase.port.dao.UserFollowDAO;
import org.springframework.stereotype.Component;

@Component
public class UserUsecase {
    private UserFollowDAO userFollowDAO;

    public UserUsecase(UserFollowDAO userFollowDAO) {
        this.userFollowDAO = userFollowDAO;
    }

    public void follow(Long followerId, Long followingId) {
        userFollowDAO.follow(followerId, followingId);
    }

    public void unfollow(Long followerId, Long followingId) {
        userFollowDAO.unfollow(followerId, followingId);
    }

    public Boolean isFollowing(Long followerId, Long followingId) {
        return userFollowDAO.isFollowing(followerId, followingId);
    }

    public List<Long> following(Long followerId, Long limit, Long offset) {
        return userFollowDAO.following(followerId, limit, offset);
    }

    public Long followingCount(Long followerId) {
        return userFollowDAO.followingCount(followerId);
    }

    public List<Long> follower(Long followingId, Long limit, Long offset) {
        return userFollowDAO.follower(followingId, limit, offset);
    }

    public Long followerCount(Long followingId) {
        return userFollowDAO.followerCount(followingId);
    }
}
