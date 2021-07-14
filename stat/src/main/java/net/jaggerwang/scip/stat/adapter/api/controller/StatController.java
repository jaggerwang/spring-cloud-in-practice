package net.jaggerwang.scip.stat.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.ApiResult;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jagger Wang
 */
@RestController
@RequestMapping("/stat")
public class StatController extends AbstractController {
    @GetMapping("/ofUser")
    public ApiResult<UserStatDTO> user(@RequestParam Long userId) {
        var userStat = statUsecase.userStatInfoByUserId(userId);

        return new ApiResult(UserStatDTO.fromBO(userStat));
    }

    @GetMapping("/ofPost")
    public ApiResult<PostStatDTO> post(@RequestParam Long postId) {
        var postStat = statUsecase.postStatInfoByPostId(postId);

        return new ApiResult(PostStatDTO.fromBO(postStat));
    }
}
