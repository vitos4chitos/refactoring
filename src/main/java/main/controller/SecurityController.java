package main.controller;

import main.database.service.entity_service.CustomerUserDetailService;
import main.entity.request.AuthUser;
import main.entity.request.RegUserForm;
import main.entity.responce.BaseAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class SecurityController {

    @Autowired
    CustomerUserDetailService customerUserDetailService;

    @PostMapping(value = "/auth")
    ResponseEntity<BaseAnswer> auth(@RequestBody AuthUser user) {
        return customerUserDetailService.auth(user);
    }

    @PostMapping("/signUp")
    ResponseEntity<BaseAnswer> signUp(@RequestBody RegUserForm user) throws ParseException {
        return customerUserDetailService.singUp(user);
    }

    @PostMapping(value = "/auth-official")
    ResponseEntity<BaseAnswer> authOfficial(@RequestBody AuthUser user) {
        return customerUserDetailService.authOfficial(user);
    }


}
