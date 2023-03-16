package main.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.entity.*;
import main.service.CustomerUserDetailService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class SecurityController {

    @Autowired
    CustomerUserDetailService customerUserDetailService;

    @Autowired
    UserService userService;

    @PostMapping(value = "/auth")
    public String getLoginPage(@RequestBody AuthUser user) {
        System.out.println("****");
        System.out.println(user.login);
        UserDetails securityUser = customerUserDetailService.loadUserByUsername(user.login);
        if (securityUser != null) {
            System.out.println(securityUser.getPassword());
            if (securityUser.getPassword().equals(user.password)) {
                System.out.println("User exist");
                return "{\"token\": \"true\"}";
            }
            return "{\"token\": \"bad\"}";
        } else {
            System.out.println("User doesn't exist");
            System.out.println("POST request ... ");
            return "{\"token\": \"bad\"}";
        }
    }

    @PostMapping("/signUp")
    public String signUp(@RequestBody RegUserForm user) throws ParseException {
        System.out.println("sdsdsd");
        User us = new User();
        us.setInstance_id((long) 1);
        us.setActive(user.isActive());
        us.setLogin(user.getUsername());
        us.setMoney(user.getMoney());
        us.setName(user.getName());
        String hashPassword = new BCryptPasswordEncoder(12).encode(user.getPassword());
        us.setPassword(hashPassword);
        us.setRole(user.getRole());
        us.setSurname(user.getSurname());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(user.getDate().toString());
        long time = date.getTime();
        us.setTime_result(new Timestamp(time));
        System.out.println(us.toString());
        return customerUserDetailService.addUser(us);
    }

    @GetMapping("/auth")
    public String getAuth() {
        return "{\"auth\": \"auth\"}";
    }

    @Data
    @NoArgsConstructor
    public static class AuthUser {
        String login;
        String password;
    }

    @PostMapping(value = "/authO")
    public String getLoginPageO(@RequestBody AuthUser user) {
        System.out.println("****");
        System.out.println(user.login);
        Official official = customerUserDetailService.loadUserByUsernameO(user.getLogin());
        System.out.println(official.getName() + " " + official.getSurname());
        System.out.println(official.getPassword());
        if (official.getPassword().equals(user.password)) {
            System.out.println("User exist");
            return "{\"token\": \"true\"}";
        }
        return "{\"token\": \"bad\"}";
    }
}
