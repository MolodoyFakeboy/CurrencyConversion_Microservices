package com.rabbit.study.controller;

import com.rabbit.study.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api")
public class UserController {

    private final UserService userService;

}
