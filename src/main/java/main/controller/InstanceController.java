package main.controller;

import main.database.entity.Checker;
import main.database.entity.Instance;
import main.database.entity.Official;
import main.database.entity.Prosecutor;
import main.database.service.entity_service.InstanceService;
import main.database.service.entity_service.OfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instance")
public class InstanceController {

    @Autowired
    InstanceService instanceService;

    @Autowired
    OfficialService officialService;

    @GetMapping("/prosecutor/{id}")
    ResponseEntity<Prosecutor> getProsecutor(@PathVariable("id") Long id) {
        return instanceService.getProsecutor(id);
    }

    @GetMapping("/official/{id}")
    ResponseEntity<Official> getOfficial(@PathVariable("id") Long id) {
        return officialService.getOfficial(id);
    }

    @GetMapping("/instance/{id}")
    ResponseEntity<Instance> getInstance(@PathVariable("id") Long id) {
        return instanceService.getInstance(id);
    }

    @GetMapping("/checker/{id}")
    ResponseEntity<Checker> getChecker(@PathVariable("id") Long id) {
        return instanceService.getChecker(id);
    }

    @PostMapping("{user-login}/nextLevel")
    String transferToTheNextLevel(@PathVariable("login") String login) {
        return instanceService.transferToTheNextLevel(login);
    }
}
