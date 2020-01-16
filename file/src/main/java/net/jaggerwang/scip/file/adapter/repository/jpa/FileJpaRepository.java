package net.jaggerwang.scip.file.adapter.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import net.jaggerwang.scip.file.adapter.repository.jpa.entity.FileDo;

@Repository
public interface FileJpaRepository
        extends JpaRepository<FileDo, Long>, QuerydslPredicateExecutor<FileDo> {
}
