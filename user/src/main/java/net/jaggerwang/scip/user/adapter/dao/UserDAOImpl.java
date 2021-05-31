package net.jaggerwang.scip.user.adapter.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQuery;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.jaggerwang.scip.user.adapter.dao.jpa.UserFollowRepository;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.User;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.UserFollow;
import net.jaggerwang.scip.user.usecase.port.dao.UserDAO;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.user.adapter.dao.jpa.UserRepository;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.QUser;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.QUserFollow;
import net.jaggerwang.scip.common.entity.UserEntity;

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
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(User.fromEntity(userEntity)).toEntity();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id).map(User::toEntity);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username).map(User::toEntity);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email).map(User::toEntity);
    }

    @Override
    public Optional<UserEntity> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile).map(User::toEntity);
    }

    @Override
    public void follow(Long followerId, Long followingId) {
        userFollowRepository.save(UserFollow.builder().followerId(followerId).followingId(followingId).build());
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
    public List<UserEntity> following(Long followerId, Long limit, Long offset) {
        var query = followingQuery(followerId);
        query.orderBy(QUserFollow.userFollow.createdAt.desc());
        if (limit != null) {
            query.limit(limit);
        }
        if (offset != null) {
            query.offset(offset);
        }

        return query.fetch().stream().map(User::toEntity).collect(Collectors.toList());
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
    public List<UserEntity> follower(Long followingId, Long limit, Long offset) {
        var query = followerQuery(followingId);
        query.orderBy(QUserFollow.userFollow.createdAt.desc());
        if (limit != null) {
            query.limit(limit);
        }
        if (offset != null) {
            query.offset(offset);
        }

        return query.fetch().stream().map(User::toEntity).collect(Collectors.toList());
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
