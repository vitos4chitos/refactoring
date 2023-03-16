package main.controller;

import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserService userService;


    @GetMapping("/main")
    String sayHelloMethod(@RequestParam Long id) {
        return userService.getUserName(id);
    }

}
