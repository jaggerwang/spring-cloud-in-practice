package net.jaggerwang.scip.user.adapter.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.jpa.impl.JPAQueryFactory;
import net.jaggerwang.scip.user.adapter.repository.jpa.RoleJpaRepository;
import org.springframework.stereotype.Component;
import net.jaggerwang.scip.user.adapter.repository.jpa.entity.QRoleDo;
import net.jaggerwang.scip.user.adapter.repository.jpa.entity.QUserDo;
import net.jaggerwang.scip.user.adapter.repository.jpa.entity.QUserRoleDo;
import net.jaggerwang.scip.user.adapter.repository.jpa.entity.RoleDo;
import net.jaggerwang.scip.user.entity.RoleEntity;
import net.jaggerwang.scip.user.usecase.port.repository.RoleRepository;

@Component
public class RoleRepositoryImpl implements RoleRepository {
    private JPAQueryFactory jpaQueryFactory;
    private RoleJpaRepository roleJpaRepo;

    public RoleRepositoryImpl(JPAQueryFactory jpaQueryFactory, RoleJpaRepository roleJpaRepo) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.roleJpaRepo = roleJpaRepo;
    }

    @Override
    public RoleEntity save(RoleEntity roleEntity) {
        return roleJpaRepo.save(RoleDo.fromEntity(roleEntity)).toEntity();
    }

    @Override
    public Optional<RoleEntity> findById(Long id) {
        return roleJpaRepo.findById(id).map(RoleDo::toEntity);
    }

    @Override
    public Optional<RoleEntity> findByName(String name) {
        return roleJpaRepo.findByName(name).map(RoleDo::toEntity);
    }

    @Override
    public List<RoleEntity> rolesOfUser(Long userId) {
        var role = QRoleDo.roleDo;
        var userRole = QUserRoleDo.userRoleDo;
        var query = jpaQueryFactory.selectFrom(role).join(userRole).on(role.id.eq(userRole.roleId))
                .where(userRole.userId.eq(userId));
        return query.fetch().stream().map(RoleDo::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<RoleEntity> rolesOfUser(String username) {
        var role = QRoleDo.roleDo;
        var userRole = QUserRoleDo.userRoleDo;
        var user = QUserDo.userDo;
        var query = jpaQueryFactory.selectFrom(role).join(userRole).on(role.id.eq(userRole.roleId)).join(user)
                .on(user.id.eq(userRole.userId)).where(user.username.eq(username));
        return query.fetch().stream().map(RoleDo::toEntity).collect(Collectors.toList());
    }
}
