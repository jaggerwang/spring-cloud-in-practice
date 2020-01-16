package net.jaggerwang.scip.user.adapter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController extends AbstractController {
    @GetMapping("/")
    public String index(Model model) {
        if (loggedUserId() != null) {
            var userEntity = userUsecases.info(loggedUserId()).get();
            model.addAttribute("loggedUser", userEntity);
        }

        return "index";
    }
}
