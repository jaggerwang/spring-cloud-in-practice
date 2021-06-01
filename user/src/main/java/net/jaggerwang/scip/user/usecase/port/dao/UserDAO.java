package net.jaggerwang.scip.user.usecase.port.dao;

import java.util.List;
import java.util.Optional;

import net.jaggerwang.scip.common.entity.UserBO;

public interface UserDAO {
    UserBO save(UserBO userBO);

    Optional<UserBO> findById(Long id);

    Optional<UserBO> findByUsername(String username);

    Optional<UserBO> findByMobile(String mobile);

    Optional<UserBO> findByEmail(String email);

    void follow(Long followerId, Long followingId);

    void unfollow(Long followerId, Long followingId);

    List<UserBO> following(Long followerId, Long limit, Long offset);

    Long followingCount(Long followerId);

    List<UserBO> follower(Long followingId, Long limit, Long offset);

    Long followerCount(Long followingId);

    Boolean isFollowing(Long followerId, Long followingId);
}
