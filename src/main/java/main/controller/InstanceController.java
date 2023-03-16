package main.controller;

import main.database.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instance")
public class InstanceController {

    @Autowired
    InstanceService instanceService;

    @GetMapping("/prosecutor/{id}")
    String getProsecutor(@PathVariable("id") Long id) {
        return instanceService.getProsecutorId(id).toString();
    }

    @GetMapping("/official/{id}")
    String getOfficial(@PathVariable("id") Long id) {
        return instanceService.getOfficialById(id).toString();
    }

    @GetMapping("/instance/{id}")
    String getInstance(@PathVariable("id") Long id) {
        return instanceService.getInstanceId(id).toString();
    }

    @GetMapping("/checker/{id}")
    String getChecker(@PathVariable("id") Long id) {
        return instanceService.getCheckerId(id).toString();
    }

    @PostMapping("{user-login}/nextLevel")
    String transferToTheNextLevel(@PathVariable("login") String login) {
        return instanceService.transferToTheNextLevel(login);
    }
}
