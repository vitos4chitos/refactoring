package main.controller;

import main.entity.*;
import main.database.service.entity_service.CustomerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class SecurityController {

    @Autowired
    CustomerUserDetailService customerUserDetailService;

    @PostMapping(value = "/auth")
    public String auth(@RequestBody AuthUser user) {
       return customerUserDetailService.auth(user);
    }

    @PostMapping("/signUp")
    public String signUp(@RequestBody RegUserForm user) throws ParseException {
       return customerUserDetailService.singUp(user);
    }

    @PostMapping(value = "/auth-official")
    public String authOfficial(@RequestBody AuthUser user) {
        return customerUserDetailService.authOfficial(user);
    }


}
