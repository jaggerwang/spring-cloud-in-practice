package net.jaggerwang.scip.user.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.sync.FileSyncService;
import net.jaggerwang.scip.common.usecase.port.service.sync.StatSyncService;
import net.jaggerwang.scip.user.adapter.controller.dto.UserDto;
import net.jaggerwang.scip.user.entity.UserEntity;
import net.jaggerwang.scip.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

abstract public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserUsecase userUsecase;

    @Autowired
    protected FileSyncService fileSyncService;

    @Autowired
    protected StatSyncService statSyncService;

    protected Long loggedUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken || !auth.isAuthenticated()) {
            return null;
        }

        var jwt = (Jwt) auth.getPrincipal();
        return Long.parseLong(jwt.getSubject());
    }

    protected UserDto fullUserDto(UserEntity userEntity) {
        var userDto = UserDto.fromEntity(userEntity);

        if (userDto.getAvatarId() != null) {
            var avatar = fileSyncService.info(userDto.getAvatarId());
            userDto.setAvatar(avatar);
        }

        userDto.setStat(statSyncService.ofUser(userDto.getId()));

        if (loggedUserId() != null) {
            userDto.setFollowing(userUsecase.isFollowing(loggedUserId(), userDto.getId()));
        }

        return userDto;
    }
}
