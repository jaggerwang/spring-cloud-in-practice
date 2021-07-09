package net.jaggerwang.scip.user.adapter.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.jaggerwang.scip.user.adapter.dao.jpa.UserFollowRepository;
import net.jaggerwang.scip.user.adapter.dao.jpa.UserRepository;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.QUser;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.QUserFollow;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.User;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.UserFollow;
import net.jaggerwang.scip.user.usecase.port.dao.UserDAO;
import net.jaggerwang.scip.common.entity.UserBO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDAOImpl implements UserDAO {
    private JPAQueryFactory jpaQueryFactory;
    private UserRepository userRepository;
    private UserFollowRepository userFollowRepository;

    public UserDAOImpl(JPAQueryFactory jpaQueryFactory, UserRepository userRepository,
                       UserFollowRepository userFollowRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.userRepository = userRepository;
        this.userFollowRepository = userFollowRepository;
    }

    @Override
    public UserBO save(UserBO userBO) {
        return userRepository.save(User.fromBO(userBO)).toBO();
    }

    @Override
    public Optional<UserBO> findById(Long id) {
        return userRepository.findById(id).map(User::toBO);
    }

    @Override
    public Optional<UserBO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(User::toBO);
    }

    @Override
    public Optional<UserBO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(User::toBO);
    }

    @Override
    public Optional<UserBO> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile).map(User::toBO);
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

    private JPAQuery<User> followingQuery(Long followerId) {
        var query = jpaQueryFactory.selectFrom(QUser.user).join(QUserFollow.userFollow)
                .on(QUser.user.id.eq(QUserFollow.userFollow.followingId));
        if (followerId != null) {
            query.where(QUserFollow.userFollow.followerId.eq(followerId));
        }
        return query;
    }

    @Override
    public List<UserBO> following(Long followerId, Long limit, Long offset) {
        var query = followingQuery(followerId);
        query.orderBy(QUserFollow.userFollow.createdAt.desc());
        if (limit != null) {
            query.limit(limit);
        }
        if (offset != null) {
            query.offset(offset);
        }

        return query.fetch().stream().map(User::toBO).collect(Collectors.toList());
    }

    @Override
    public Long followingCount(Long followerId) {
        return followingQuery(followerId).fetchCount();
    }

    private JPAQuery<User> followerQuery(Long followingId) {
        var query = jpaQueryFactory.selectFrom(QUser.user).join(QUserFollow.userFollow)
                .on(QUser.user.id.eq(QUserFollow.userFollow.followerId));
        if (followingId != null) {
            query.where(QUserFollow.userFollow.followingId.eq(followingId));
        }
        return query;
    }

    @Override
    public List<UserBO> follower(Long followingId, Long limit, Long offset) {
        var query = followerQuery(followingId);
        query.orderBy(QUserFollow.userFollow.createdAt.desc());
        if (limit != null) {
            query.limit(limit);
        }
        if (offset != null) {
            query.offset(offset);
        }

        return query.fetch().stream().map(User::toBO).collect(Collectors.toList());
    }

    @Override
    public Long followerCount(Long followingId) {
        return followerQuery(followingId).fetchCount();
    }

    @Override
    public Boolean isFollowing(Long followerId, Long followingId) {
        return userFollowRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
}
