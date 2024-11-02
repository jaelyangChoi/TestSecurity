package org.example.testsecurity.controller;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainP(Model model) {

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.iterator().next().getAuthority();

        model.addAttribute("id", id);
        model.addAttribute("role", role);
        return "main";
    }

    @GetMapping("/beforeForward")
    public String beforeForward() {
        return "forward:/afterForward";
    }

    @GetMapping("/beforeRedirect")
    public String beforeFRedirect() {
        return "redirect:/afterRedirect";
    }

    @GetMapping("/after")
    @ResponseBody
    public String after() {
        return "after";
    }


}
