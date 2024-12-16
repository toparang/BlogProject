package me.toparang.blog.controller;

import lombok.RequiredArgsConstructor;
import me.toparang.blog.dto.AddUserRequest;
import me.toparang.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userService.save(request);
        return "redirect:/login";
    }
}
