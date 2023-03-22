package main.controller;


import main.database.entity.*;
import main.database.service.QueueAgregatorService;
import main.database.service.entity_service.QueueService;
import main.entity.responce.BaseAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<Queue> getQueue(@PathVariable("id") Long id) {
        return queueService.getQueue(id);
    }

    @GetMapping("/user/{id}")
    ResponseEntity<List<Queue>> getUsersQueues(@PathVariable("id") Long id) {
        return queueService.getAllQueueByUserId(id);
    }

    @GetMapping("/official/{login}")
    ResponseEntity<BaseAnswer> getOfficialQueue(@PathVariable("login") String login) {
        return queueAgregatorService.getOfficialQueue(login);
    }

    @GetMapping("/official/{login}/first-user")
    ResponseEntity<BaseAnswer> getFirstUserFromOfficialQueue(@PathVariable("login") String login) {
        return queueAgregatorService.getFirstUserFromOfficialQueue(login);
    }

    @PutMapping("/official/{login}/advance")
    ResponseEntity<BaseAnswer> getAdvanceQueue(@PathVariable("login") Long officialId) {
        queueService.advanceQueue(officialId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
