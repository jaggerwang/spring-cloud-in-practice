package net.jaggerwang.scip.auth.usecase;

import java.util.List;

import net.jaggerwang.scip.common.entity.RoleBO;
import net.jaggerwang.scip.auth.usecase.port.dao.RoleDAO;
import org.springframework.stereotype.Component;

/**
 * @author Jagger Wang
 */
@Component
public class AuthUsecase {
    private RoleDAO roleDAO;

    public AuthUsecase(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public List<RoleBO> rolesOfUser(Long userId) {
        return roleDAO.rolesOfUser(userId);
    }
}
