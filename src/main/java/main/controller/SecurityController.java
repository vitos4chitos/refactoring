package main.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.database.entity.Official;
import main.database.entity.User;
import main.entity.*;
import main.database.service.CustomerUserDetailService;
import main.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
