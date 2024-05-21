package ntukhpi.ddy.webjavaddylab3.controller;

import ntukhpi.ddy.webjavaddylab3.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelComeController {

    @GetMapping("/welcome")
    public String greeting() {
        return "redirect:/trains";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(name = "error", required = false) String error,
                                @RequestParam(name = "redirected", required = false) String redirected,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }

        if ("true".equals(redirected)) {
            model.addAttribute("redirected", true);
        }

        return "login";
    }
}