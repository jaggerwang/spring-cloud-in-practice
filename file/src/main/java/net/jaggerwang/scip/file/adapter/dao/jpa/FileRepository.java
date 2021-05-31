package net.jaggerwang.scip.file.adapter.dao.jpa;

import net.jaggerwang.scip.file.adapter.dao.jpa.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository
        extends JpaRepository<File, Long>, QuerydslPredicateExecutor<File> {
}
