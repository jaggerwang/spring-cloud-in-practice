package net.jaggerwang.scip.gateway.adapter.controller;

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
            var userDto = userService.info(loggedUserId());
            model.addAttribute("loggedUser", userDto);
        }

        return "index";
    }
}
