package net.jaggerwang.scip.user.adapter.dao;

import net.jaggerwang.scip.common.adapter.dao.jpa.domain.OffsetBasedPageRequest;
import net.jaggerwang.scip.user.adapter.dao.jpa.UserFollowRepository;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.UserFollow;
import net.jaggerwang.scip.user.usecase.port.dao.FollowDAO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jagger Wang
 */
@Component
public class FollowDAOImpl implements FollowDAO {
    private UserFollowRepository userFollowRepository;

    public FollowDAOImpl(UserFollowRepository userFollowRepository) {
        this.userFollowRepository = userFollowRepository;
    }

    @Override
    public void follow(Long followerId, Long followingId) {
        userFollowRepository.save(UserFollow.builder()
                .followerId(followerId)
                .followingId(followingId)
                .build());
    }

    @Override
    public void unfollow(Long followerId, Long followingId) {
        userFollowRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    public List<Long> following(Long followerId, Long limit, Long offset) {
        var pageable = new OffsetBasedPageRequest(offset, limit.intValue(),
                Sort.by("id").descending());
        var userFollows = userFollowRepository.findAllByFollowerId(followerId, pageable);

        return userFollows.stream().map(UserFollow::getFollowingId).collect(Collectors.toList());
    }

    @Override
    public Long followingCount(Long followerId) {
        return userFollowRepository.countByFollowerId(followerId);
    }

    @Override
    public List<Long> follower(Long followingId, Long limit, Long offset) {
        var pageable = new OffsetBasedPageRequest(offset, limit.intValue(),
                Sort.by("id").descending());
        var userFollows = userFollowRepository.findAllByFollowingId(followingId, pageable);

        return userFollows.stream().map(UserFollow::getFollowerId).collect(Collectors.toList());
    }

    @Override
    public Long followerCount(Long followingId) {
        return userFollowRepository.countByFollowingId(followingId);
    }

    @Override
    public Boolean isFollowing(Long followerId, Long followingId) {
        return userFollowRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
}
