package net.jaggerwang.scip.stat.adapter.api.controller;

import net.jaggerwang.scip.common.usecase.port.service.dto.PostStatDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.RootDTO;
import net.jaggerwang.scip.common.usecase.port.service.dto.UserStatDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stat")
public class StatController extends AbstractController {
    @GetMapping("/ofUser")
    public RootDTO user(@RequestParam Long userId) {
        var userStat = statUsecase.userStatInfoByUserId(userId);

        return new RootDTO().addDataEntry("userStat", UserStatDTO.fromBO(userStat));
    }

    @GetMapping("/ofPost")
    public RootDTO post(@RequestParam Long postId) {
        var postStat = statUsecase.postStatInfoByPostId(postId);

        return new RootDTO().addDataEntry("postStat", PostStatDTO.fromBO(postStat));
    }
}
