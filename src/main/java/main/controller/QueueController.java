package main.controller;


import main.database.entity.*;
import main.database.service.QueueAgregatorService;
import main.database.service.entity_service.QueueService;
import main.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/queue")
public class QueueController {

    @Autowired
    QueueService queueService;

    @Autowired
    QueueAgregatorService queueAgregatorService;

    @GetMapping("{id}")
    String getQueue(@PathVariable("id") Long id) {
        return queueService.getQueueById(id).toString();
    }

    @GetMapping("/user/{id}")
    List<Queue> getUsersQueues(@PathVariable("id") Long id) {
        return queueService.getAllQueueByUserId(id);
    }

    @GetMapping("/official/{login}")
    public List<BackQueue> getOfficialQueue(@PathVariable("login") String login) {
        return queueAgregatorService.getOfficialQueue(login);
    }

    @GetMapping("/official/{login}/first-user")
    public FirstUser getFirstUserFromOfficialQueue(@PathVariable("login") String login) {
        return queueAgregatorService.getFirstUserFromOfficialQueue(login);
    }

    @GetMapping("/official/{login}/advance")
    public Boolean getAdvanceQueue(@PathVariable("login") Long officialId) {
        return queueService.advanceQueue(officialId);
    }
}
