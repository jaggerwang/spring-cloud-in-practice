package net.jaggerwang.scip.user.adapter.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.jaggerwang.scip.user.adapter.dao.jpa.RoleRepository;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.QRole;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.QUserRole;
import net.jaggerwang.scip.user.adapter.dao.jpa.entity.Role;
import net.jaggerwang.scip.user.usecase.port.dao.RoleDAO;
import net.jaggerwang.scip.common.entity.RoleBO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Jagger Wang
 */
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
        return roleRepository.save(Role.fromBO(roleBO)).toBO();
    }

    @Override
    public Optional<RoleBO> findById(Long id) {
        return roleRepository.findById(id).map(Role::toBO);
    }

    @Override
    public Optional<RoleBO> findByName(String name) {
        return roleRepository.findByName(name).map(Role::toBO);
    }

    @Override
    public List<RoleBO> rolesOfUser(Long userId) {
        var query = jpaQueryFactory.selectFrom(QRole.role).join(QUserRole.userRole)
                .on(QRole.role.id.eq(QUserRole.userRole.roleId))
                .where(QUserRole.userRole.userId.eq(userId));
        return query.fetch().stream().map(Role::toBO).collect(Collectors.toList());
    }
}
