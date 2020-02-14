package net.jaggerwang.scip.stat.adapter.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.RootDto;
import net.jaggerwang.scip.stat.adapter.controller.dto.PostStatDto;
import net.jaggerwang.scip.stat.adapter.controller.dto.UserStatDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stat")
public class StatController extends AbstractController {
    @GetMapping("/ofUser")
    public RootDto user(@RequestParam Long userId) {
        var userStat = statUsecase.userStatInfoByUserId(userId);

        return new RootDto().addDataEntry("userStat", UserStatDto.fromEntity(userStat));
    }

    @GetMapping("/ofPost")
    public RootDto post(@RequestParam Long postId) {
        var postStat = statUsecase.postStatInfoByPostId(postId);

        return new RootDto().addDataEntry("postStat", PostStatDto.fromEntity(postStat));
    }
}
