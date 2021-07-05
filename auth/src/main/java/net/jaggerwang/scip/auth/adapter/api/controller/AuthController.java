package net.jaggerwang.scip.auth.adapter.api.controller;

import java.util.stream.Collectors;

import net.jaggerwang.scip.common.usecase.port.service.dto.RoleDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController extends AbstractController {
    @GetMapping("/rolesOfUser")
    public RootDTO roles(@RequestParam Long userId) {
        var roleEntities = authUsecase.rolesOfUser(userId);

        return new RootDTO().addDataEntry("roles",
                roleEntities.stream().map(RoleDTO::fromEntity).collect(Collectors.toList()));
    }
}
