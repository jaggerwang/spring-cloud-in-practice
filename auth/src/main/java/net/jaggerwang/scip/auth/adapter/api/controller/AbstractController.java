package net.jaggerwang.scip.auth.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.UserSyncService;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.common.usecase.port.service.FileSyncService;
import net.jaggerwang.scip.common.usecase.port.service.StatSyncService;
import net.jaggerwang.scip.common.entity.UserBO;
import net.jaggerwang.scip.auth.usecase.AuthUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

abstract public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AuthUsecase authUsecase;

    @Autowired
    protected UserSyncService userSyncService;

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

    protected Map<String, Object> fullUserDto(UserBO userBO) {
        var userDto = UserDTO.fromEntity(userBO);
        var m = objectMapper.convertValue(userDto, Map.class);

        if (userDto.getAvatarId() != null) {
            var avatar = fileSyncService.info(userDto.getAvatarId());
            m.put("avatar", avatar);
        }

        m.put("stat", statSyncService.ofUser(userDto.getId()));

        if (loggedUserId() != null) {
            m.put("following", userSyncService.isFollowing(userDto.getId()));
        }

        return m;
    }
}
