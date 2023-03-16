package main.controller;

import main.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    QueueService queueService;

    @GetMapping("{id}")
    String getSchedule(@PathVariable("id") Long id) {
        return queueService.getScheduleById(id).toString();
    }
}
