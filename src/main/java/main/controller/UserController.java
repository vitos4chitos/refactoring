package main.controller;


import main.database.service.entity_service.InstanceService;
import main.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    InstanceService instanceService;

    @GetMapping("/{login}/instance")
    UserInfo getInstance(@PathVariable("login") String login){
        return instanceService.getInstance(login);
    }
}
