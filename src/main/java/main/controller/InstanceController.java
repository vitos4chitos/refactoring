package main.controller;

import main.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instance")
public class InstanceController {

    @Autowired
    InstanceService instanceService;

    @GetMapping("/prosecutor/get")
    String getProsecutor(@RequestParam Long id) {
        return instanceService.getProsecutorId(id).toString();
    }

    @GetMapping("/official/get")
    String getOfficial(@RequestParam Long id) {
        return instanceService.getOfficialById(id).toString();
    }

    @GetMapping("/instance/get")
    String getInstance(@RequestParam Long id) {
        return instanceService.getInstanceId(id).toString();
    }

    @GetMapping("/checker/get")
    String getChecker(@RequestParam Long id) {
        return instanceService.getCheckerId(id).toString();
    }

    @GetMapping("/nextLevel")
    String transferToTheNextLevel(@RequestParam String login) {
        boolean a = instanceService.transferToTheNextLevel(login);
        if(a){
            return "{\"token\": \"true\"}";
        }
        else{
            return "{\"token\": \"false\"}";
        }
    }
}
