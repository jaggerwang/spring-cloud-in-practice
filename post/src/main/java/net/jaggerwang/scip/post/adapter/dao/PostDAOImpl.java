package net.jaggerwang.scip.post.adapter.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQuery;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.jaggerwang.scip.post.adapter.dao.jpa.entity.PostLike;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.post.adapter.dao.jpa.PostRepository;
import net.jaggerwang.scip.post.adapter.dao.jpa.entity.Post;
import net.jaggerwang.scip.post.adapter.dao.jpa.entity.QPost;
import net.jaggerwang.scip.post.adapter.dao.jpa.entity.QPostLike;
import net.jaggerwang.scip.post.adapter.dao.jpa.PostLikeRepository;
import net.jaggerwang.scip.common.entity.PostBO;
import net.jaggerwang.scip.post.usecase.port.dao.PostDAO;

@Component
public class PostDAOImpl implements PostDAO {
    private PostRepository postRepository;

    private PostLikeRepository postLikeRepository;

    protected JPAQueryFactory jpaQueryFactory;

    public PostDAOImpl(PostRepository postRepository, PostLikeRepository postLikeRepository,
                       JPAQueryFactory jpaQueryFactory) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public PostBO save(PostBO postBO) {
        return postRepository.save(Post.fromBO(postBO)).toBO();
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Optional<PostBO> findById(Long id) {
        return postRepository.findById(id).map(post -> post.toBO());
    }

    private JPAQuery<Post> publishedQuery(Long userId) {
        var query = jpaQueryFactory.selectFrom(QPost.post);
        if (userId != null) {
            query.where(QPost.post.userId.eq(userId));
        }
        return query;
    }

    @Override
    public List<PostBO> published(Long userId, Long limit, Long offset) {
        var query = publishedQuery(userId);
        query.orderBy(QPost.post.createdAt.desc());
        if (limit != null) {
            query.limit(limit);
        }
        if (offset != null) {
            query.offset(offset);
        }

        return query.fetch().stream().map(post -> post.toBO()).collect(Collectors.toList());
    }

    @Override
    public Long publishedCount(Long userId) {
        return publishedQuery(userId).fetchCount();
    }

    @Override
    public void like(Long userId, Long postId) {
        postLikeRepository.save(PostLike.builder().userId(userId).postId(postId).build());
    }

    @Override
    public void unlike(Long userId, Long postId) {
        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
    }

    private JPAQuery<Post> likedQuery(Long userId) {
        var query = jpaQueryFactory.selectFrom(QPost.post).join(QPostLike.postLike)
                .on(QPost.post.id.eq(QPostLike.postLike.postId));
        if (userId != null) {
            query.where(QPostLike.postLike.userId.eq(userId));
        }
        return query;
    }

    @Override
    public List<PostBO> liked(Long userId, Long limit, Long offset) {
        var query = likedQuery(userId);
        query.orderBy(QPostLike.postLike.createdAt.desc());
        if (limit != null) {
            query.limit(limit);
        }
        if (offset != null) {
            query.offset(offset);
        }

        return query.fetch().stream().map(post -> post.toBO()).collect(Collectors.toList());
    }

    @Override
    public Long likedCount(Long userId) {
        return likedQuery(userId).fetchCount();
    }

    @Override
    public Boolean isLiked(Long userId, Long postId) {
        return postLikeRepository.existsByUserIdAndPostId(userId, postId);
    }

    private JPAQuery<Post> followingQuery(List<Long> userIds) {
        var query = jpaQueryFactory.selectFrom(QPost.post);
        query.where(QPost.post.userId.in(userIds));
        return query;
    }

    @Override
    public List<PostBO> following(List<Long> userIds, Long limit, Long beforeId, Long afterId) {
        var query = followingQuery(userIds);
        query.orderBy(QPost.post.createdAt.desc());
        if (limit != null) {
            query.limit(limit);
        }
        if (beforeId != null) {
            query.where(QPost.post.id.lt(beforeId));
        }
        if (afterId != null) {
            query.where(QPost.post.id.gt(afterId));
        }

        return query.fetch().stream().map(post -> post.toBO()).collect(Collectors.toList());
    }

    @Override
    public Long followingCount(List<Long> userIds) {
        return followingQuery(userIds).fetchCount();
    }
}
