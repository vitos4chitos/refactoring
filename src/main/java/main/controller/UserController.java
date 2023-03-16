package main.controller;


import lombok.Data;
import lombok.NoArgsConstructor;
import main.database.service.InstanceService;
import main.entity.UserInfo;
import main.database.service.CustomerUserDetailService;
import main.database.service.UserService;
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
