package net.jaggerwang.scip.post.adapter.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import net.jaggerwang.scip.post.adapter.dao.jpa.entity.Post;

@Repository
public interface PostRepository
        extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post> {
}
