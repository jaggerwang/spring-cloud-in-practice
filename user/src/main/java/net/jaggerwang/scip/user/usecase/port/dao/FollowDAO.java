package net.jaggerwang.scip.user.usecase.port.dao;

import java.util.List;

/**
 * @author Jagger Wang
 */
public interface FollowDAO {
    void follow(Long followerId, Long followingId);

    void unfollow(Long followerId, Long followingId);

    List<Long> following(Long followerId, Long limit, Long offset);

    Long followingCount(Long followerId);

    List<Long> follower(Long followingId, Long limit, Long offset);

    Long followerCount(Long followingId);

    Boolean isFollowing(Long followerId, Long followingId);
}
