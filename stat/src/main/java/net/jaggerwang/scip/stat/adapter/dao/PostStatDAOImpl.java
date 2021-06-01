package net.jaggerwang.scip.stat.adapter.dao;

import java.util.Optional;

import net.jaggerwang.scip.stat.adapter.dao.jpa.entity.PostStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.stat.adapter.dao.jpa.PostStatRepository;
import net.jaggerwang.scip.common.entity.PostStatBO;
import net.jaggerwang.scip.stat.usecase.port.dao.PostStatDAO;

@Component
public class PostStatDAOImpl implements PostStatDAO {
    @Autowired
    private PostStatRepository postStatRepository;

    @Override
    public PostStatBO save(PostStatBO postStatBO) {
        return postStatRepository.save(PostStat.fromEntity(postStatBO)).toEntity();
    }

    @Override
    public Optional<PostStatBO> findById(Long id) {
        return postStatRepository.findById(id).map(postStat -> postStat.toEntity());
    }

    @Override
    public Optional<PostStatBO> findByPostId(Long postId) {
        return postStatRepository.findByPostId(postId).map(postStat -> postStat.toEntity());
    }
}
