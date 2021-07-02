package net.jaggerwang.scip.auth.adapter.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.jaggerwang.scip.auth.adapter.dao.jpa.RoleRepository;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.auth.adapter.dao.jpa.entity.QRole;
import net.jaggerwang.scip.auth.adapter.dao.jpa.entity.QUser;
import net.jaggerwang.scip.auth.adapter.dao.jpa.entity.QUserRole;
import net.jaggerwang.scip.auth.adapter.dao.jpa.entity.Role;
import net.jaggerwang.scip.common.entity.RoleBO;
import net.jaggerwang.scip.auth.usecase.port.dao.RoleDAO;

@Component
public class RoleDAOImpl implements RoleDAO {
    private JPAQueryFactory jpaQueryFactory;
    private RoleRepository roleRepository;

    public RoleDAOImpl(JPAQueryFactory jpaQueryFactory, RoleRepository roleRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleBO save(RoleBO roleBO) {
        return roleRepository.save(Role.fromEntity(roleBO)).toEntity();
    }

    @Override
    public Optional<RoleBO> findById(Long id) {
        return roleRepository.findById(id).map(Role::toEntity);
    }

    @Override
    public Optional<RoleBO> findByName(String name) {
        return roleRepository.findByName(name).map(Role::toEntity);
    }

    @Override
    public List<RoleBO> rolesOfUser(Long userId) {
        var query = jpaQueryFactory.selectFrom(QRole.role).join(QUserRole.userRole)
                .on(QRole.role.id.eq(QUserRole.userRole.roleId))
                .where(QUserRole.userRole.userId.eq(userId));
        return query.fetch().stream().map(Role::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<RoleBO> rolesOfUser(String username) {
        var query = jpaQueryFactory.selectFrom(QRole.role).join(QUserRole.userRole)
                .on(QRole.role.id.eq(QUserRole.userRole.roleId)).join(QUser.user)
                .on(QUser.user.id.eq(QUserRole.userRole.userId)).where(QUser.user.username.eq(username));
        return query.fetch().stream().map(Role::toEntity).collect(Collectors.toList());
    }
}
