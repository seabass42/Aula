package com.tutorportal.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    /**
     * Only the GET is ours. Spring Security intercepts POST /login itself --
     * there is deliberately no handler method for it.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
