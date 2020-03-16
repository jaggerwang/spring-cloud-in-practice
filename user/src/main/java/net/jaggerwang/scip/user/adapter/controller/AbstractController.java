package net.jaggerwang.scip.user.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDto;
import net.jaggerwang.scip.common.usecase.port.service.sync.FileSyncService;
import net.jaggerwang.scip.common.usecase.port.service.sync.StatSyncService;
import net.jaggerwang.scip.common.entity.UserEntity;
import net.jaggerwang.scip.user.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

abstract public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserUsecase userUsecase;

    @Autowired
    protected FileSyncService fileSyncService;

    @Autowired
    protected StatSyncService statSyncService;

    @Autowired
    protected HttpServletRequest request;

    protected Long loggedUserId() {
        var xUserId = request.getHeader("X-User-Id");
        if (StringUtils.isEmpty(xUserId)) return null;
        return Long.parseLong(xUserId);
    }

    protected Map<String, Object> fullUserDto(UserEntity userEntity) {
        var userDto = UserDto.fromEntity(userEntity);
        var m = objectMapper.convertValue(userDto, Map.class);

        if (userDto.getAvatarId() != null) {
            var avatar = fileSyncService.info(userDto.getAvatarId());
            m.put("avatar", avatar);
        }

        m.put("stat", statSyncService.ofUser(userDto.getId()));

        if (loggedUserId() != null) {
            m.put("following", userUsecase.isFollowing(loggedUserId(), userDto.getId()));
        }

        return m;
    }
}
