package net.jaggerwang.scip.user.adapter.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserDTO;
import net.jaggerwang.scip.common.usecase.port.service.FileService;
import net.jaggerwang.scip.common.usecase.port.service.StatService;
import net.jaggerwang.scip.common.entity.UserBO;
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
    protected FileService fileService;

    @Autowired
    protected StatService statService;

    @Autowired
    protected HttpServletRequest request;

    protected Long loggedUserId() {
        var xUserId = request.getHeader("X-User-Id");
        if (StringUtils.isEmpty(xUserId)) return null;
        return Long.parseLong(xUserId);
    }

    protected Map<String, Object> fullUserDto(UserBO userBO) {
        var userDto = UserDTO.fromBO(userBO);
        var m = objectMapper.convertValue(userDto, Map.class);

        if (userDto.getAvatarId() != null) {
            var avatar = fileService.info(userDto.getAvatarId());
            m.put("avatar", avatar);
        }

        m.put("stat", statService.ofUser(userDto.getId()));

        if (loggedUserId() != null) {
            m.put("following", userUsecase.isFollowing(loggedUserId(), userDto.getId()));
        }

        return m;
    }
}
