package main.controller;

import main.database.entity.Schedule;
import main.database.service.entity_service.QueueService;
import main.database.service.entity_service.ScheduleService;
import main.entity.BaseAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @GetMapping("{id}")
    ResponseEntity<Schedule> getSchedule(@PathVariable("id") Long id) {
        return scheduleService.getSchedule(id);
    }
}
