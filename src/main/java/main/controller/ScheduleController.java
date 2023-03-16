package main.controller;

import main.database.service.entity_service.QueueService;
import main.database.service.entity_service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @GetMapping("{id}")
    String getSchedule(@PathVariable("id") Long id) {
        return scheduleService.getScheduleById(id).toString();
    }
}
