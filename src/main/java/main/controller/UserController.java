package main.controller;


import lombok.Data;
import lombok.NoArgsConstructor;
import main.entity.User;
import main.entity.UserInfo;
import main.service.CustomerUserDetailService;
import main.service.DocumentService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CustomerUserDetailService customerUserDetailService;

    @GetMapping("/{login}/instance")
    UserInfo getInstance(@PathVariable("login") String login){
        Long user = userService.getUserId(login);
        if(user == -1){
            return null;
        }
        else{
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userService.getUserById(user).getInstance_id());
            userInfo.setMoney(userService.getUserById(user).getMoney());
            System.out.println(userInfo.getId());
            return userInfo;
        }


    }
    @Data
    @NoArgsConstructor
    public static class AuthUser {
        String login;
        String password;
    }
}
